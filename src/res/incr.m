n = 10;
 
 v3 = importdata('proc\k3mainp') + importdata('proc\k3sortp');
 
 avg3 = zeros(1, 8);
 err3 = zeros(1, 8);
 
 ref = mean(importdata('proc\kssortp')) + mean(importdata('proc\ksmainp'));
 
 for i = 1 : 8
     left = 1 + (i - 1) * 10;
     right = i * 10;
     
     data = v3(left : right);
     
     for j = 1 : length(data)
         data(j) = ref / data(j);
     end
     
     avg3(i) = mean(data);
     err3(i) = 1.96 * std(data, 0, 1) / sqrt(n);
 end
 
 v2 = importdata('proc\k2mainp') + importdata('proc\k2sortp');
 
 avg2 = zeros(1, 8);
 err2 = zeros(1, 8);
 
 for i = 1 : 8
     left = 1 + (i - 1) * 10;
     right = i * 10;
     
     data = v2(left : right);
     
     for j = 1 : length(data)
         data(j) = ref / data(j);
     end
     
     avg2(i) = mean(data);
     err2(i) = 1.96 * std(data, 0, 1) / sqrt(n);
 end

v1 = importdata('proc\k1mainp') + importdata('proc\k1sortp');

avg1 = zeros(1, 8);
err1 = zeros(1, 8);

for i = 1 : 8
    left = 1 + (i - 1) * 10;
    right = i * 10;
    
    data = v1(left : right);
    
    for j = 1 : length(data)
        data(j) = ref / data(j);
    end
    
    avg1(i) = mean(data);
    err1(i) = 1.96 * std(data, 0, 1) / sqrt(n);
end

errorbar(1:8, avg1, err1)
hold on;
errorbar(1 : 8, avg2, err2);
errorbar(1 : 8, avg3, err3);
legend('Version 1', 'Version 2', 'Version 3', 'Location', 'southeast');

xlabel('Number of processors');
ylabel('Speed-up');
title('Iterative improvements');

xlim([0.5 8.5]);

grid on;