function [L,U,P] = lup_decomposition(A)
n = size(A,1);
P = zeros(n,n);
piv = (1:n)';
for k=1:n-1
    % pivot
    [~,index] = max(abs(A(k:n,k)));
    index = index + k - 1;

    % interchange
    if index ~= k
        A([k, index],:) = A([index, k],:);
        piv([k,index]) = piv([index,k]);
    end
    % Schur complement
    lin = k + 1:n;
    A(lin,k) = A(lin,k) / A(k,k);
    A(lin,lin) = A(lin,lin) - A(lin,k) * A(k,lin);
end
for k=1:n
    P(k,piv(k)) = 1;
end
U = triu(A);
L = tril(A,-1) + eye(n);
end