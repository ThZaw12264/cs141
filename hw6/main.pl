my_length([], 0).
my_length([_|T], R) :- my_length(T, R2), R is R2 + 1.

my_member(A, []) :- false.
my_member(A, [A|_]).
my_member(A, [_|T]) :- my_member(A, T).

my_append([], L2, L2).
my_append([H|T], L2, [H|R]) :- my_append(T, L2, R).

my_reverse(L,R) :- my_reverse_helper(L,R,[]).
my_reverse_helper([], RL, RL).
my_reverse_helper([H|T], R, RL) :- my_reverse_helper(T, R, [H|RL]).

my_nth([], _, []).
my_nth(L, 1, L).
my_nth([H|T], N, R) :- N>1, N2 is N-1, my_nth(T, N2, R).

my_remove(X,[],[]).
my_remove(X,[X|T],R) :- my_remove(X,T,R).
my_remove(X,[H|T],[H|R]) :- my_remove(X,T,R).

my_subst(X,Y,[],[]).
my_subst(X,Y,[X|T],[Y|R]) :- my_subst(X,Y,T,R).
my_subst(X,Y,[H|T],[H|R]) :- my_subst(X,Y,T,R).

my_subset(P,[],[]).
my_subset(P,[H|T],[H|R]) :- Eval=..[P,H], call(Eval), my_subset(P,T,R), !.
my_subset(P,[H|T],R) :- my_subset(P,T,R).

my_add(L1, L2, R) :- my_add_helper(L1, L2, R, 0).
my_add_helper([], [], [C], C) :- C =:= 1, !.
my_add_helper([], [], [], _).
my_add_helper([], [H1|T], [Sum|T1], C) :- Sum is H1+C, H1 + C < 10, !.
my_add_helper([], [H1|T], [Sum|R], C) :- Sum is ((H1+C) mod 10), my_add_helper([], T, R, 1).
my_add_helper([H1|T], [], [Sum|R], C) :- Sum is H1+C, H1 + C < 10, !.
my_add_helper([H1|T], [], [Sum|R], C) :- Sum is ((H1+C) mod 10), my_add_helper(T, [], R, 1).
my_add_helper([H1|T1], [H2|T2], [Sum|R], C) :- Sum is ((H1+H2+C) mod 10), NewC is ((H1+H2+C) // 10), my_add_helper(T1, T2, R, NewC).

my_merge([], L2, L2).
my_merge(L1, [], L1).
my_merge([H1|T1], [H2|T2], [H1|R]) :- H1 < H2, !, my_merge(T1, [H2|T2], R).
my_merge(L1, [H2|T2], [H2|R]) :- my_merge(L1, T2, R).

my_sublist(_,[]) :- false.
my_sublist([H|T1], [H|T2]) :- my_sublist_helper(T1, T2).
my_sublist(L1, [_|T2]) :- my_sublist(L1, T2).
my_sublist_helper([], L2) :-true.
my_sublist_helper([H|T1], [H|T2]) :- my_sublist_helper(T1, T2).

my_assoc(_, [], _) :- false.
my_assoc(A, [A,V|T], V).
my_assoc(A, [_,_|T], R) :- my_assoc(A, T, R).

my_replace(_, [], []).
my_replace(ALIST, [H|T], [V|R]) :- my_assoc(H, ALIST, V), my_replace(ALIST, T, R).
my_replace(ALIST, [H|T], [H|R]) :- my_replace(ALIST, T, R).