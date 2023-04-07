function x = gauss_solve(C, b)
dett = det(C);
n = size(C, 1);
if dett == 0
    disp("det = 0")
else
    A = [ C  b ];
    for j = 1:(n-1)
        for i= (j+1) : n
            multiplicator = A(i,j) / A(j,j);
            for k= j:n+1
                A(i,k) = A(i,k) - multiplicator*A(j,k);
            end
        end
    end

    x(n) = A(n,n+1) / A(n,n);
    for i=n-1:-1:1
        suma = 0;
        for j=i+1:n
            suma = suma + A(i,j) * x(j);
            x(i) = (A(i,n+1) - suma) / A(i,i);
        end
    end
    x = x';
end
end