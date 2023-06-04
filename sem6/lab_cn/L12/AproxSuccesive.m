function [z, k] = AproxSuccesive(f, fd, x0, ea, er, iter_max)
     %f - functia
    %fd - derivata
    %x0 - aproximatia initiala 
    %ea - eroarea absoluta
    %er - eroarea relativa
    %itre_max - numarul maxim de iteratii
    %Iesire
    %z - aproximatia radacinii
    %ni - numarul de iteratii
    
    if nargin < 6, iter_max = 50; end
    if nargin < 5, er = 0; end
    if nargin < 4, ea = 1e-3; end

    x_prev = x0;
    lambda = fd(x0);

    for k = 1 : iter_max
        x_curr = x_prev - lambda \ f(x_prev);
        if norm(x_curr - x_prev, inf) < ea + er *norm(x_curr, inf)
            z  = x_curr; %succes
            return;
        end
        x_prev = x_curr;
    end
    error('S-a depășit numărul maxim de iterații');
end