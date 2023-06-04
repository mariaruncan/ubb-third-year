function [z, k] = secanta(f, x0, x1, ea, er, iter_max)
    %f - functia
    %x0, x1 - valori de pornire
    %ea, er - eroarea absoluta, resp eroarea relativa
    %iter_max - numarul maxim de iteratii
    %iesire
    %z - aproximatia radacinii
    %ni - numar de iteratii
    
    if nargin<6, iter_max = 50; end
    if nargin<5, er = 0; end
    if nargin<4, ea = 1e-3; end

    x_prev = x0; 
    fprev = f(x_prev); 
    x_curr = x1; 
    fcurr = f(x_curr);

    for k = 1 : iter_max
        xn = x_curr - fcurr * (x_curr - x_prev) / (fcurr - fprev);
        if abs(xn - x_curr) < ea + er * xn %succes
            z = xn;
            return
        end
        x_prev = x_curr; 
        fprev = fcurr; 
        x_curr = xn; 
        fcurr = f(xn);
    end
    
    error('numarul maxim de iteratii depasit')
end