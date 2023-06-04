function [g_nodes, g_coeff] = Legendre(n)
    beta = [2, (4 - ([1 : n - 1]).^(-2)).^(-1)];
    alpha = zeros(n, 1);
    [g_nodes, g_coeff] = Gauss_quad(alpha, beta);
end