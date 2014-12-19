n = 10;

list = importdata('kbdl');

avgl = zeros(1, 8);
errl = zeros(1, 8);

for i = 1 : 8
    left = 1 + (i - 1) * 10;
    right = i * 10;
    
    ref = 5.8534 * 1000.0;
    
    data = list(left : right);
    
    for j = 1 : length(data)
        data(j) = ref / data(j);
    end
    
    avgl(i) = mean(data);
    errl(i) = 1.96 * std(data, 0, 1) / sqrt(n);
end

mat = importdata('kbdm');

avgm = zeros(1, 8);
errm = zeros(1, 8);

for i = 1 : 8
    left = 1 + (i - 1) * 10;
    right = i * 10;
    
    ref = 5.8426 * 1000.0;
    
    data = mat(left : right);
    
    for j = 1 : length(data)
        data(j) = ref / data(j);
    end
    
    avgm(i) = mean(data);
    errm(i) = 1.96 * std(data, 0, 1) / sqrt(n);
end

errorbar(1 : 8, avgl, errl);
hold on;
errorbar(1 : 8, avgm, errm);
legend('Adjacency list', 'Adjacency Matrix', 'Location', 'southeast');

xlabel('Number of processors');
ylabel('Speed-up');
title('Kruskal - Graph representations');

xlim([0.5 8.5]);

grid on;