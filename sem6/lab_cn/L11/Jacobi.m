function [g_nodes, g_coeff] = Jacobi(n, a, b)
    a0 = (b - a) / (b + a + 2);
    b0 = 2 ^ (a + b + 1) * gamma(a + 1) * gamma(b + 1) / gamma(a + b + 2);
    if n == 1
        alpha = a0; bet = b0;
    else
        k1 = 1 : n-1;
        k2 = 2 : n-1;
        b1 = 4 * (1 + a) * (1 + b) / ((2 + a + b) ^ 2 * (3 + a + b));
        if a == b
            alpha = zeros(1, n);
        else
            alpha = [a0, (b^2 - a^2) ./ (2*k1 + a + b) ./ (2*k1 + a + b + 2)];
        end
        bet = [b0, b1, 4*k2 .* (k2 + a + b) .* (k2 + a) .*...
            (k2 + b) ./ (2*k2 + a + b - 1) ./ (2*k2 + a + b) .^ 2 ./ (2*k2 + a + b + 1)];
    end
    [g_nodes, g_coeff] = Gauss_quad(alpha, bet);
end