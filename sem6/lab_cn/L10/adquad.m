function Q = adquad(f, a, b, prec)

    if nargin < 4 
        prec = 1.e-6;
    end

    c = (a + b)/2;
    fa = f(a); fb = f(b); fc = f(c);

    Q = quadstep(f, a, b, prec, fa, fc, fb);
end