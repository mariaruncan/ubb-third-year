function x = lup_solve(A, b)
[L, U, P] = lup_decomposition(A);
% Solve Ly = Pb for y using forward substitution
y = forward_subst(L, P * b);
% Solve Ux = y for x using backward substitution
x = back_subst(U, y);
end