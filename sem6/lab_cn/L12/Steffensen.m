function [z, k] = Steffensen(f, x0, ea, er, iter_max)
    %f - functia
    %x0 - valoare de pornire
    %ea, er - eroarea absoluta, resp eroarea relativa
    %iter_max - numarul maxim de iteratii
    %iesire
    %z - aproximatia radacinii
    %ni - numar de iteratii
    
    if nargin<6, iter_max = 50; end
    if nargin<5, er = 0; end
    if nargin<4, ea = 1e-3; end

    for k = 1 : iter_max
        x1 = f(x0);
        x2 = f(x1);
        x = x0 - ((x1 - x0)^2 / (x2 - 2*x1 + x0));

        if abs(x - x0) < ea + er * x %succes
            z = x;
            return
        end
        x0 = x;
    end
    error('numarul maxim de iteratii depasit')
end