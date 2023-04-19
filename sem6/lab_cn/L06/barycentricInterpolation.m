function ff=barycentricInterpolation(x,y,xx,c)
    n = length(x);
    numer = zeros(size(xx));
    denom = zeros(size(xx));
    exact = zeros(size(xx));
    for j=1:n
        xdiff = xx - x(j);
        temp = c(j) ./ xdiff;
        numer = numer + temp * y(j);
        denom = denom + temp;
        exact(xdiff==0) = j;
    end
    ff = numer ./ denom;
    jj = find(exact);
    ff(jj) = y(exact(jj));
end