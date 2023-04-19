function [x, iter]=solveGaussSeidel(A, b, tol, x0, iterMax)

if nargin < 5, iterMax = 1000; end
if nargin < 4, x0 = zeros(size(A, 1), 1); end
if nargin < 3, tol = 1e-3; end

N = -triu(A, 1);
M = A + N;

normVal = Inf;
iter = 0;
x = x0;

while normVal > tol * norm(x, inf) && iter < iterMax
    x0 = x;

    x = M \ (N * x0 + b);

    normVal = norm(x - x0, inf);

    iter = iter + 1;
end

end