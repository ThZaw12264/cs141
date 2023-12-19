eval(X,X) :- number(X).
eval(X+Y,V) :- eval(X,XV), eval(Y,YV), V is XV + YV.
eval(X-Y,V) :- eval(X,XV), eval(Y,YV), V is XV - YV.
eval(X*Y,V) :- eval(X,XV), eval(Y,YV), V is XV * YV.
eval(X/Y,V) :- eval(X,XV), eval(Y,YV), V is XV / YV.
eval(X^Y,V) :- eval(X,XV), eval(Y,YV), V is XV ^ YV.


numbers(XS,YS) :- number(XS), number(YS).

simplify(X,X) :- number(X).
simplify(X,X) :- atom(X).

check_base(X*0).
check_base(X*1).
check_base(X/0).
check_base(X/1).
check_base(X+0).
check_base(X-0).
check_base(X^0).
check_base(X^1).

check_base(0*X).
check_base(1*X).
check_base(0/X).
check_base(0+X).
check_base(0-X).
check_base(0^X).
check_base(1^X).

check_base(X*Y*X/X) :- atom(X), number(Y).

base(X*0,0).
base(X*1,X).
base(X/0,S) :- fail.
base(X/1,X).
base(X+0,X).
base(X-0,X).
base(X^0,1).
base(X^1,X).

base(0*X,0).
base(1*X,X).
base(0/X,0).
base(0+X,X).
base(0-X,-X).
base(0^X,0).
base(1^X,1).

base(X*Y*X/X,Y*X) :- atom(X), number(Y).

simplify(X+Y,S) :- simplify(X,XS), simplify(Y,YS), (numbers(XS,YS) -> S is XS + YS; check_base(XS + YS) -> base(XS + YS, XYS), S = XYS; S = XS + YS).
simplify(X-Y,S) :- simplify(X,XS), simplify(Y,YS), (numbers(XS,YS) -> S is XS - YS; check_base(XS - YS) -> base(XS - YS, XYS), S = XYS; S = XS - YS).
simplify(X*Y,S) :- simplify(X,XS), simplify(Y,YS), (numbers(XS,YS) -> S is XS * YS; check_base(XS * YS) -> base(XS * YS, XYS), S = XYS; S = XS * YS).
simplify(X/Y,S) :- simplify(X,XS), simplify(Y,YS), (numbers(XS,YS) -> S is XS / YS; check_base(XS / YS) -> base(XS / YS, XYS), S = XYS; S = XS / YS).
simplify(X^Y,S) :- simplify(X,XS), simplify(Y,YS), (numbers(XS,YS) -> S is XS ^ YS; check_base(XS ^ YS) -> base(XS ^ YS, XYS), S = XYS; S = XS ^ YS).


deriv_helper(X,0) :- number(X).
deriv_helper(X,1) :- atom(X).
deriv_helper(Z*X,Z) :- atom(X), number(Z).
deriv_helper(X^Y,Y*X^NewY) :- atom(X), number(Y), NewY is Y - 1.
deriv_helper(Z*X^Y,NewZ*X^NewY) :- atom(X), number(Z), number(Y), NewZ is Z * Y, NewY is Y - 1.
deriv_helper(X/Z,1/Z) :- atom(X), number(Z).
deriv_helper(Z/X,NewZ/X^2) :- number(Z), atom(X), NewZ is Z * -1.
deriv_helper(Z/Z2*X,newZ/Z2*X^2) :- number(Z), number(Z2), atom(X), NewZ is Z * -1.
deriv_helper(Z/X^Y,NewZ/X^NewY) :- number(Z), number(Y), atom(X), NewZ is Z * Y * -1, NewY is Y + 1.
deriv_helper(Z/Z2*X^Y,NewZ/Z2*X^NewY) :- number(Z), number(Y), number(Z2), atom(X), NewZ is Z * Y * -1, NewY is Y + 1.

deriv_helper(A+Z/X,AD-Z/X^2) :- number(Z), atom(X), deriv_helper(A,AD).
deriv_helper(A+Z/Z2*X,AD-Z/Z2*X^2) :- number(Z), number(Z2), atom(X), deriv_helper(A,AD).
deriv_helper(A+Z/X^Y,AD-NewZ/X^NewY) :- number(Z), number(Y), atom(X), NewZ is Z * Y, NewY is Y + 1, deriv_helper(A,AD).
deriv_helper(A+Z/Z2*X^Y,AD-NewZ/Z2*X^NewY) :- number(Z), number(Y), number(Z2), atom(X), NewZ is Z * Y, NewY is Y + 1, deriv_helper(A,AD).
deriv_helper(A-Z/X,AD+Z/X^2) :- number(Z), atom(X), deriv_helper(A,AD).
deriv_helper(A-Z/Z2*X,AD+Z/Z2*X^2) :- number(Z), number(Z2), atom(X), deriv_helper(A,AD).
deriv_helper(A-Z/X^Y,AD+NewZ/X^NewY) :- number(Z), number(Y), atom(X), NewZ is Z * Y, NewY is Y + 1, deriv_helper(A,AD).
deriv_helper(A-Z/Z2*X^Y,AD+NewZ/Z2*X^NewY) :- number(Z), number(Y), number(Z2), atom(X), NewZ is Z * Y, NewY is Y + 1, deriv_helper(A,AD).

deriv(E,D) :- simplify(E,S), deriv_helper(S,D2), simplify(D2,D), !.
deriv_helper(X+Y,D) :- deriv_helper(X,XD), deriv_helper(Y,YD), D = XD + YD.
deriv_helper(X-Y,D) :- deriv_helper(X,XD), deriv_helper(Y,YD), D = XD - YD.


# male(klefstad).
# male(bill).
# male(mark).
# male(isaac).
# male(fred).
# female(emily).
# female(heidi).
# female(beth).
# female(susan).
# female(jane).
# speaks(klefstad, english).
# speaks(bill, english).
# speaks(emily, english).
# speaks(heidi, english).
# speaks(isaac, english).
# speaks(beth, french).
# speaks(mark, french).
# speaks(susan, french).
# speaks(isaac, french).
# speaks(klefstad, spanish).
# speaks(bill, spanish).
# speaks(susan, spanish).
# speaks(fred, spanish).
# speaks(jane, spanish).

person(X) :- male(X).
person(X) :- female(X).
gender_compatible(X,Y) :- female(X), male(Y).
gender_compatible(X,Y) :- male(X), person(Y).
last(X,[X]).
last(X,[_|Z]) :- last(X,Z).
language_compatible([H|T]) :- last(X,[H|T]), L2 = [X|[H|T]], append(L2,[H],L3), language_compatible_helper(L3).
language_compatible_helper([H1,H2,H3]) :- speaks(H1,Lang1), speaks(H2,Lang1), speaks(H2,Lang2), speaks(H3,Lang2).
language_compatible_helper([H1,H2,H3|T]) :- speaks(H1,Lang1), speaks(H2,Lang1), speaks(H2,Lang2), speaks(H3,Lang2), language_compatible_helper([H2,H3|T]).
distinct([]).
distinct([H|T]) :- not(member(H,T)), distinct(T).
party_seating(L) :- person(X), party_seating_helper(L,[X],1,X).
party_seating_helper(AccList,[H|T],9,First) :- person(X), gender_compatible(H,X), gender_compatible(X,First), AccList = [X,H|T], distinct(AccList), language_compatible(AccList).
party_seating_helper(L,[H|T],N,First) :- person(X), gender_compatible(H,X), AccList = [X,H|T], distinct(AccList), NewN is N+1, party_seating_helper(L,AccList,NewN,First).