function [x, iter] = solveSOR(A, b, omega, tol, iterMax)
n = length(b);

if nargin < 5, iterMax = 1000; end
if nargin < 4, tol = 1e-3; end
if nargin < 3, omega = 1-pi^2/2/(n+1)^2; end

x0 = zeros(n, 1);
x = x0;
iter = 0;
err = inf;

while err > tol && iter < iterMax
    x0 = x;
    for i = 1:n
        summ = 0;
        for j = 1:n
            if j ~= i
                summ = summ + A(i,j)*x(j);
            end
        end
        x(i) = (1 - omega) * x(i) + (omega / A(i,i)) * (b(i)-summ);
    end
    iter = iter + 1;
    err = norm(x - x0);
end

end