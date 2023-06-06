function R = myPadeApprox(f, m, n, x)
    first = zeros(n, n, 'sym');
    for i = 1:n
        for j = 1:n
            % minunea
            tmp = m + i - j;
            if tmp > 0
                first(i, j) = subs(diff(f, x, tmp), 0) / factorial(tmp);
            end
        end
    end
    
    third = zeros(n, 1, 'sym');
    for i = 1:n
        tmp = m + i;
        third(i) = - subs(diff(f, x, tmp), 0) / factorial(tmp);
    end
    
    bs = zeros(max(m, n) + 1,1,'sym');
    bs(2:n+1) = first \ third;
    bs(1) = 1;
    bs = bs';
    
    as = zeros(m + 1, 1);
    syms j
    for j = 0:m
        as(j + 1) = sym('0');
        for l = 0:j
            tmp = j - l;
            as(j + 1) = as(j + 1) + (subs(diff(f, x, tmp), 0) / factorial(tmp)) * bs(l + 1);
        end
    end
    
    p = sym('0');
    for i = 0:m
        p = p + as(i + 1) * x^i;
    end
    
    q = sym('0');
    for j = 0:n
        q = q + bs(j + 1) * x^j;
    end
    
    R = p / q;
end