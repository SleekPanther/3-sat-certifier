# 3-SAT Certifier (3 Satisfiability Problem)
A certifier algorithm for the NP-Complete 3-Satisfiability Problem

## Problem Statement
**Given a Conjunctive Normal Form formula, is there a satisfying truth assignment so that it evaluates to true?**  
![](images/cnf.png)  
- Each clause must have the same number of literals (literals are X<sub>1</sub>, X<sub>2</sub>, X<sub>3</sub>, etc.)  
This is the 3-SAT problem so each clause has **exactly 3 literals**  
**But the code is generic enough to work on ANY NUMBER OF LITERALS in a clause (given that each clause has the same number)**
- The literals in a clauses can have their value flipped using **NOT, boolean negation**
- In each clause, literals (or their negation) are combined with **compound boolean OR**
- All clauses are combined with **compound boolean AND**
- The final result of the CNF is either `1` or `0` (`true` or `false`)
- **The 3-SAT problem asks if this result for all clauses is `true`**

## Certifier Algorithm
This 3-SAT problem is NP-Complete, this not a solution to the problem
**Instead, given a certificate of truth assignments, does the CNF evaluate to true?**  

## Code Details
- Literals must be **"X<sub>i</sub>" where i is an integer** 
- Input for the CNF formula is a String with **"AND"** and **"OR"** spelled out
- String parsing is **case-INsensitive**
- Input for certificates a `String` array. Each one in the form:
  - `(x1=1, x2=1, x3=0, x4=1)`

## Sample Certificates
The program parses the following strings to check if they're valid certificates to the CNF  
`(NOT x1 OR X2 OR x3) AND (x1 OR NOT x2 OR x3) AND (x1 OR x2 OR x4) AND (NOT x1 OR NOT x3 OR NOT x4)`
1. `(x1=1, x2=1, x3=0, x4=1)` **valid**
2. `(x1=1, x2=1, x3=1, x4=1)` **INVALID**
3. `(x1=0, x2=0, x3=0, x4=1)` **valid**
4. `(x1=0, x2=1, x3=0, x4=1)` **INVALID**
