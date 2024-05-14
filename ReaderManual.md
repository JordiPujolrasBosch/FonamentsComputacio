# READER MANUAL

## Automatons

---

### Format

````
states: [number-states]
start: [start-state]
final: [final-states]
alphabet: [alphabet-tokens]
[transitions]
````

### Keywords

#### 1) [number-states]

**Description:** How many states the automaton have.

**Conditions:**
* `[number-states] > 0`.

**Examples:** `2`, `5`, `10` ...

#### 2) [start-state]

**Description:** Code of the start state.

**Conditions:**
* `[start-state] >= 0` and `[start-state] < [number-states]`.

**Examples:** `0`, `1`, `3` ...

#### 3) [final-states]

**Description:** List of codes of final states separated with `,`.

**Conditions:**
* For every `[code]` in the list: `[code] >= 0` and `[code] < [number-states]`.
* The list must have at least 1 element.
* If the automaton doesn't have any final state, `[final-states]` must be a nagative number.

**Examples:** `0, 1, 2`, `0, 5, 7, 3`, `-1`, `11` ...

#### 4) [alphabet-tokens]

**Description:** List of tokens separated with `,`.

**Conditions:**
* Each token in the list must be a character or an `[a-token]`.

**Examples:** `$/, 0, 1`, `$0, $c, $/, +, -`, `$a, $A, $s, .`, `$w` ...

#### 5) [transitions]

**Description:** List of `[transition-rule]`. Each element of the list must be on a different line.

#### 6) [transition-rule]

**Description:** Rule that describes a transition of the automaton.

**Format:** `[o] [c] [d]`.

**Elements:**
* `[o]` Code of the origin state.
* `[d]` Code of the destiny state.
* `[c]` Character of the transition.

**Conditions:**
* `[o] >= 0` and `[o] < [number-states]`.
* `[d] >= 0` and `[d] < [number-states]`.
* `[c]` must be a character or a `[b-token]`.
* `[c]` must be a character included in `[alphabet-tokens]`.

**Examples:** `0 a 1`, `3 $/ 5`, `4 t 5`, `0 0 0`, `10 $s 6` ...

### Differences between dfa and nfa

The deterministic finite automatons (dfa):
* Must not have the empty character `$/` on the alphabet.
* Must not have two transitions with the same origin and the same character.
* If not all the possibles transitions are written, it assumes that those transitions go to a (new) sink-state.

The nondeterministic finite automaton (nfa):
* Must have the empty character `$/` on the alphabet.
* Can have two (or more) transitions with the same origin and the same character.

### Examples

The next 4 automatons are equivalent. They accept the word `aba`.
* Deterministic automaton with an automatic sink-state.
```
states: 4
start: 0
final: 3
alphabet: a, b
0 a 1
1 b 2
2 a 3
```
* Deterministic automaton with all the transitions written.
```
states: 5
start: 0
final: 3
alphabet: a, b, $w
0 a 1
0 b 4
1 a 4
1 b 2
2 a 3
2 b 4
3 a 4
3 b 4
4 a 4
4 b 4
```
* Nondeterministic automaton (minimum for word `aba`)
```
states: 4
start: 0
final: 3
alphabet: a, b, $/
0 a 1
1 b 2
2 a 3
```
* Nondeterministic automaton (not minimum for word `aba`)
```
states: 7
start: 0
final: 5
alphabet: a, b, $/
0 a 1
1 $/ 2
2 b 3
3 $/ 4
3 $/ 6
4 a 5
```

## Regular expressions

---

The regular expressions are written with characters and `[r-token]`. 
The space is skipped. The expression can be written in multiple lines.

**Examples:** `abc+`, `a(b|c)a`, `My $s name $s is: $s $A $a*.`,

`0 | (($+|-|/) (1|2|3|4|5|6|7|8|9) $0*)`,

`(0 | (($+|-|/) (1|2|3|4|5|6|7|8|9) $0*)) . $0+`,

```
America
| Europe
| Asia
| Africa
| Antartica
| Oceania
```

## Context free grammars

---

### Format

```
terminals: [alphabet-tokens]
variables: [variables-list]
start: [start-variable]
[cfg-rules]
```

### Keywords

#### 1) [alphabet-tokens]

**Description:** List of tokens separated with `,`.

**Conditions:**
* Each token in the list must be a character or an `[a-token]`.

**Examples:** `$/, 0, 1`, `$0, $c, $/, +, -`, `$a, $A, $s, .`, `$w` ...

#### 2) [variables-list]

**Description:** List of variables separated with `,`.

**Conditions:**
* Each element of the list must be a `[cfg-variable]` or a `[cfg-var-group]`.

**Examples:** `A0, B0, B1`, `A-F, A2`, `B0-7, C0-9` ...

#### 3) [cfg-variable]

**Description:** A variable (non-terminal) for a context free grammar.

**Format:** `[upper-letter][natural-number]`

**Conditions:**
* It must not be a space between the letter and the number.

**Examples:** `A0`, `B2`, `C5`, `X0` ...

#### 4) [cfg-var-group]

**Description:** A short way to write many `[cfg-variable]`.

**Option 1:** 
* **Format:** `[upper-letter-a]-[upper-letter-b]`.
* **Conditions:** `[upper-letter-a] < [upper-letter-b]`.
* **Examples:**
  * `A-D` represents `A0, B0, C0, D0`.
  * `C-H` represents `C0, D0, E0, F0, G0, H0`.

**Option 2:**
* **Format:** `[upper-letter][natural-number-a]-[natural-number-b]`.
* **Conditions:** `[natural-number-a] < [natural-number-b]`.
* **Examples:**
  * `A0-5` represents `A0, A1, A2, A3, A4, A5`.
  * `E4-7` represents `E4, E5, E6, E7`.
  * `G12-15` represents `G12, G13, G14, G15`.

#### 5) [start-variable]

**Description:** `[cfg-variable]` of the start state.

**Conditions:**
* `[start-variable]` must be variable included in `[variables-list]`.

**Examples:** `A0`, `C1`, `C3` ...

#### 6) [cfg-rules]

**Description:** List of `[grammar-rule]`. Each element of the list must be on a different line.

#### 7) [grammar-rule]

**Description:** Rule that describes one (or more) substitutions of the grammar.

**Format:** `[left] -> [right]`

**Elements:**
* `[left]`: `[cfg-variable]` to substitute.
* `[right]`: possible substitutions of `[left]` separated with `|`.

**Conditions:**
* `[left]` must be a variable included in `[variables-list]`.
* `[right]` is a combination of: 
  * Characters included in `[alphabet-tokens]`
  * Variables included in `[variables-list]`
  * `[g-token]`
* All the elements in `[right]` must be separated with spaces.
* Cannot be concatenated: `/`, `$a`, `$A`, `$0`.

**Examples:** `A0 -> A1 A2`, `B0 -> A1 c B3 | / | a b`, 
`A1 -> a | b | c | d d`,`D11 -> / | A1 A2 A3 | B0 $s`, `N0 -> $0` ...

## Tokens

---

### List of [a-token]
* `$/`: Represents the empty character.
* `$a`: Represents the letters `a` to `z`.
* `$A`: Represents the letters `A` to `Z`.
* `$0`: Represents the numbers `0` to `9`.
* `$s`: Represents the character space: `' '`.
* `$c`: Represents the character comma: `,`.
* `$w`: Represents nothing.

### List of [b-token]
* `$/`: Represents the empty character.
* `$s`: Represents the character space: `' '`.

### List of [r-token]
* `(`: Open priority parenthesis
* `)`: Close priority parenthesis
* `|`: Regex union
* `*`: Regex star
* `+`: Regex plus
* `#`: Regex void
* `/`: Regex empty char


* `$(`: Character `(`
* `$)`: Character `)`
* `$|`: Character `|`
* `$*`: Character `*`
* `$+`: Character `+`
* `$#`: Character `#`
* `$/`: Character `/`


* `$s`: Character space `' '`
* `$$`: Character `$`
* `$0`: Union of numbers: `(0|1|2|3|4|5|6|7|8|9)`
* `$a`: Union of letters: `(a|b|c| ... |x|y|z)`
* `$A`: Union of letters: `(A|B|C| ... |X|Y|Z)`

`$?` is ilegal where `?` is not: `()|*+#/s$0aA`

### List of [g-token]
* `/`: Empty char
* `|`: Union
* `$/`: Character `/`
* `$|`: Character `|`
* `$s`: Character space `' '`
* `$0`: Union of numbers: `0|1|2|3|4|5|6|7|8|9`
* `$a`: Union of letters: `a|b|c| ... |x|y|z`
* `$A`: Union of letters: `A|B|C| ... |X|Y|Z`

`$?` is ilegal where `?` is not: `/|s0aA`