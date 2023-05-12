function P=calculateLagrangePolynomial(x, y, xx)

    P = zeros(size(xx));
    m = length(x);
    
    for k=1:m
        L = ones(size(xx));
        for j=1:m
            if k~=j
                L = L .* ((xx - x(j)) / (x(k) - x(j)));
            end
        end

        P = P + L * y(k);
    end
end