function [result, iter] = romberg(f, a, b, iter_no, prec)
    if nargin < 5
        prec = 1E-3;
    end

    h = b - a;
    R = zeros(iter_no, iter_no);
    R(1, 1) = (h / 2) * (f(a) + f(b));
    
    for k = 2:iter_no
        sum = 0;
        for i = 1:(2^(k-2))
            sum = sum + f(a + (i - 1/2) * h);
        end
        R(k, 1) = (1/2) * (R(k-1, 1) + h * sum);
        
        for j = 2:k
            % R(k, j) = R(k, j-1) + (R(k, j-1) - R(j-1, k-1))/(4^(j-1) - 1);
            R(k, j) = ((4^(j-1)) * R(k, j - 1) - R(k - 1, j-1)) / (4^(j-1) - 1);
        end

        if(abs(R(k, k) - R(k - 1, k - 1)) < prec)
            result = R(k, k);
            iter = k;
            return;
        end

        h = h / 2;
    end
    
    result = R(iter_no, iter_no);
    iter = iter_no;
end
