package Factory;

import AutomatonElements.Alphabet;
import AutomatonElements.State;
import Automatons.Pda;
import ContextFreeGrammars.*;
import Factory.Constructors.CfgConstructor;
import Factory.Constructors.PdaConstructor;

import java.util.*;

public class GrammarFactory {
    public static Cfg chomsky(Cfg x){
        CfgConstructor ccx = x.getConstructor();

        addNewStart(ccx);
        eliminateEmptyRules(ccx);
        removeUnusedVars(ccx);
        removeUnitRules(ccx);
        makeRulesBinary(ccx);
        replaceBinaryTerminals(ccx);

        return ccx.getCfg();
    }

    public static Pda cfgToPda(Cfg x) {
        CfgConstructor ccx = x.getConstructor();
        PdaConstructor pc = new PdaConstructor();

        State start = new State();
        State accept = new State();
        State loop =  new State();

        pc.states.add(start);
        pc.states.add(accept);
        pc.states.add(loop);

        pc.start = start;

        pc.finalStates.add(accept);

        pc.alphabet.addAll(ccx.terminals);
        pc.alphabet.addEmptyChar();
        pc.alphabet.addChar(Alphabet.getStackChar());

        for(Character c : pc.alphabet.set()) pc.addMapper(c);
        for(CfgVariable v : ccx.variables) pc.addMapper(v);
        pc.addMapper(Alphabet.getEmptyChar());

        int e = pc.getMapper(Alphabet.getEmptyChar());
        State mid = new State();
        pc.states.add(mid);
        pc.transition.add(start, e, e, mid, pc.getMapper(Alphabet.getStackChar()));
        pc.transition.add(mid, e, e, loop, pc.getMapper(ccx.start));
        pc.transition.add(loop, e, pc.getMapper(Alphabet.getStackChar()), accept, e);

        for(Character c : ccx.terminals.set()){
            pc.transition.add(loop, pc.getMapper(c), pc.getMapper(c), loop, e);
        }

        buildRules(pc, ccx, loop);

        return pc.getCfg();
    }

    private static void buildRules(PdaConstructor pc, CfgConstructor ccx, State loop){
        int e = pc.getMapper(Alphabet.getEmptyChar());
        for(CfgRule r : ccx.rules){
            if(r.right().length() == 0) {
                pc.transition.add(loop, e, pc.getMapper(r.left()), loop, e);
            }
            else if(r.right().length() == 1){
                int k = 0;
                if(r.right().type() == TypesRight.VAR) k = pc.getMapper(r.right().toRightVar().v());
                else k = pc.getMapper(r.right().toRightChar().c());
                pc.transition.add(loop, e, pc.getMapper(r.left()), loop, k);
            }
            else{
                List<Integer> list = makeList(r.right(), pc);

                State pre = null;
                State post = null;
                for(int i = list.size()-1; i >= 0; i--){
                    if(i == list.size()-1){
                        pre = loop;
                        post = new State();
                        pc.states.add(post);
                        pc.transition.add(pre, e, pc.getMapper(r.left()), post, list.get(i));
                    }
                    else if(i == 0){
                        pre = post;
                        post = loop;
                        pc.transition.add(pre, e, e, post, list.get(i));
                    }
                    else{
                        pre = post;
                        post = new State();
                        pc.states.add(post);
                        pc.transition.add(pre, e, e, post, list.get(i));
                    }
                }
            }
        }
    }

    private static List<Integer> makeList(Right right, PdaConstructor pc) {
        List<Integer> list = new ArrayList<>();
        if(right.type() == TypesRight.VAR){
            list.add(pc.getMapper(right.toRightVar().v()));
        }
        else if(right.type() == TypesRight.CHAR){
            list.add(pc.getMapper(right.toRightChar().c()));
        }
        else{
            RightNonEmpty a = right.toRightConcat().a();
            RightNonEmpty b = right.toRightConcat().b();
            list.addAll(makeList(a, pc));
            list.addAll(makeList(b, pc));
        }
        return list;
    }

    //Chomsky private

    private static void addNewStart(CfgConstructor ccx) {
        CfgVariable newStart = ccx.generate(ccx.start);
        ccx.rules.add(new CfgRule(newStart, new RightVar(ccx.start)));
        ccx.start = newStart;
    }

    private static void eliminateEmptyRules(CfgConstructor ccx) {
        Set<CfgRule> emptyRules = new HashSet<>();
        for(CfgRule r : ccx.rules){
            if(r.isEmptyRule()) emptyRules.add(r);
        }

        Set<CfgRule> removed = new HashSet<>();
        while(!emptyRules.isEmpty()){
            if(emptyRules.size() == 1 && emptyRules.iterator().next().left().equals(ccx.start)){
                emptyRules.clear();
            }
            else{
                //Get a rule
                Iterator<CfgRule> it = emptyRules.iterator();
                CfgRule act = it.next();
                if(act.left().equals(ccx.start)) act = it.next();

                //Remove act
                ccx.rules.remove(act);
                removed.add(act);

                //Add rules with occurrences of act.left
                Set<CfgRule> toadd = new HashSet<>();
                for(CfgRule r : ccx.rules) toadd = rulesToAddStepTwo(r, act.left(), removed);
                ccx.rules.addAll(toadd);
                for(CfgRule r : toadd){
                    if(r.isEmptyRule()) emptyRules.add(r);
                }

                //Rule removed
                emptyRules.remove(act);
            }
        }
    }

    private static void removeUnusedVars(CfgConstructor ccx){
        Set<CfgVariable> set = new HashSet<>(ccx.variables);
        for(CfgRule r : ccx.rules) set.remove(r.left());

        ccx.variables.removeAll(set);

        Set<CfgRule> rules = new HashSet<>();
        for(CfgRule r : ccx.rules){
            for(CfgVariable v : set){
                if(r.right().containsVar(v)) rules.add(r);
            }
        }

        ccx.rules.removeAll(rules);
    }

    private static void removeUnitRules(CfgConstructor ccx) {
        Set<CfgRule> unitRules = new HashSet<>();
        for(CfgRule r : ccx.rules){
            if(r.right().type() == TypesRight.VAR) unitRules.add(r);
        }

        Set<CfgRule> removed = new HashSet<>();
        while(!unitRules.isEmpty()){
            CfgRule rule = unitRules.iterator().next();
            CfgVariable ri = rule.right().toRightVar().v();

            if(rule.left().equals(ri)){
                ccx.rules.remove(rule);
                removed.add(rule);
                unitRules.remove(rule);
            }
            else{
                Set<CfgRule> toadd = new HashSet<>();
                for(CfgRule r : ccx.rules){
                    if(r.left().equals(ri)){
                        CfgRule aux = new CfgRule(rule.left(), r.right());
                        if(aux.right().type() != TypesRight.VAR) toadd.add(aux);
                        else if(!removed.contains(aux)){
                            toadd.add(aux);
                            unitRules.add(aux);
                        }
                    }
                }

                ccx.rules.addAll(toadd);
                ccx.rules.remove(rule);
                removed.add(rule);
                unitRules.remove(rule);
            }
        }
    }

    private static void makeRulesBinary(CfgConstructor ccx) {
        boolean foundNonBinaryRule = true;
        while(foundNonBinaryRule){
            foundNonBinaryRule = false;
            CfgRule nonBinaryRule = null;

            Iterator<CfgRule> it = ccx.rules.iterator();
            while(it.hasNext() && !foundNonBinaryRule){
                nonBinaryRule = it.next();
                foundNonBinaryRule = nonBinaryRule.right().length() > 2;
            }

            if(foundNonBinaryRule){
                RightConcat pair = nonBinaryRule.right().toRightConcat().getPair();
                CfgRule newRule = new CfgRule(ccx.generate(new CfgVariable('Z',0)), pair);

                Set<CfgRule> toremove = new HashSet<>();
                Set<CfgRule> toadd = new HashSet<>();
                for(CfgRule r : ccx.rules){
                    if(r.right().containsPair(pair)){
                        RightNonEmpty substituted = r.right().toRightConcat().getChanged(pair, newRule.left());
                        toremove.add(r);
                        toadd.add(new CfgRule(r.left(), substituted));
                    }
                }
                ccx.rules.addAll(toadd);
                ccx.rules.removeAll(toremove);
                ccx.rules.add(newRule);
            }
        }
    }

    private static void replaceBinaryTerminals(CfgConstructor ccx) {
        boolean foundBinaryTerminalRules = true;
        while(foundBinaryTerminalRules){
            foundBinaryTerminalRules = false;
            CfgRule binaryTerminalRule = null;

            Iterator<CfgRule> it = ccx.rules.iterator();
            while(it.hasNext() && !foundBinaryTerminalRules){
                binaryTerminalRule = it.next();
                foundBinaryTerminalRules = binaryTerminalRule.right().length() == 2 && binaryTerminalRule.right().containsTerminal();
            }

            if(foundBinaryTerminalRules){
                RightConcat rc = binaryTerminalRule.right().toRightConcat();
                Character c = null;
                if(rc.a().type() == TypesRight.CHAR) c = rc.a().toRightChar().c();
                else c = rc.b().toRightChar().c();

                CfgRule newRule = new CfgRule(ccx.generate(new CfgVariable('Z',0)), new RightChar(c));

                Set<CfgRule> toremove = new HashSet<>();
                Set<CfgRule> toadd = new HashSet<>();
                for(CfgRule r : ccx.rules){
                    if(r.right().length() == 2 && r.right().containsTerminal(c)){
                        Right substituted = r.right().getChanged(c, newRule.left());
                        toremove.add(r);
                        toadd.add(new CfgRule(r.left(), substituted));
                    }
                }
                ccx.rules.addAll(toadd);
                ccx.rules.removeAll(toremove);
                ccx.rules.add(newRule);
            }
        }
    }


    private static Set<CfgRule> rulesToAddStepTwo(CfgRule rule, CfgVariable toremove, Set<CfgRule> removed){
        if(!rule.right().containsVar(toremove)) return new HashSet<>();

        Set<CfgRule> set = new HashSet<>();
        if(rule.right().type() == TypesRight.VAR){
            CfgRule aux = new CfgRule(rule.left(), new RightEmpty());
            if(!removed.contains(aux)) set.add(aux);
        }
        else{
            Set<Right> setright = stepTwoIm(rule.right().toRightConcat(), toremove);
            for(Right r : setright){
                CfgRule aux = new CfgRule(rule.left(), r);
                if(r.type() == TypesRight.EMPTY && !removed.contains(aux)) set.add(aux);
                else set.add(aux);
            }
        }
        return set;
    }

    private static Set<Right> stepTwoIm(RightConcat right, CfgVariable toremove){
        Set<Right> set = new HashSet<>();

        TypesRight var = TypesRight.VAR;
        TypesRight con = TypesRight.CONCAT;
        TypesRight ta = right.a().type();
        TypesRight tb = right.b().type();
        boolean ina = right.a().containsVar(toremove);
        boolean inb = right.b().containsVar(toremove);

        if(ina && inb){
            if(ta == con && tb == con){
                set.addAll(stepTwoConAB(right, toremove));
            }
            else if(ta == var && tb == con){
                set.addAll(stepTwoIm(right.b().toRightConcat(), toremove));
            }
            else if(ta == con && tb == var){
                set.addAll(stepTwoIm(right.a().toRightConcat(), toremove));
            }
            else if(ta == var && tb == var){
                set.add(new RightEmpty());
            }
        }

        if(ta == con && ina) set.addAll(stepTwoConA(right, toremove));
        if(tb == con && inb) set.addAll(stepTwoConB(right, toremove));
        if(ta == var && ina) set.add(right.b());
        if(tb == var && inb) set.add(right.a());

        return set;
    }

    private static Set<Right> stepTwoConA(RightConcat right, CfgVariable toremove){
        Set<Right> set = new HashSet<>();
        for(Right r : stepTwoIm(right.a().toRightConcat(), toremove)){
            if(r.type() == TypesRight.EMPTY) set.add(right.b());
            else set.add(new RightConcat(r.toRightNonEmpty(), right.b()));
        }
        return set;
    }

    private static Set<Right> stepTwoConB(RightConcat right, CfgVariable toremove){
        Set<Right> set = new HashSet<>();
        for(Right r : stepTwoIm(right.b().toRightConcat(), toremove)){
            if(r.type() == TypesRight.EMPTY) set.add(right.a());
            else set.add(new RightConcat(right.a(), r.toRightNonEmpty()));
        }
        return set;
    }

    private static Set<Right> stepTwoConAB(RightConcat right, CfgVariable toremove){
        Set<Right> set = new HashSet<>();
        for(Right ra : stepTwoIm(right.a().toRightConcat(), toremove)){
            for(Right rb : stepTwoIm(right.b().toRightConcat(), toremove)){
                if(ra.type() == TypesRight.EMPTY && rb.type() == TypesRight.EMPTY) set.add(ra);
                else if(ra.type() == TypesRight.EMPTY) set.add(rb);
                else if(rb.type() == TypesRight.EMPTY) set.add(ra);
                else set.add(new RightConcat(ra.toRightNonEmpty(), rb.toRightNonEmpty()));
            }
        }
        return set;
    }

}
