wilcox.test(res$metric_x, res$metric_y, paired=TRUE,exact = F,conf.level = 0.99)# <0.05 :signifant else not
cliff.delta(res$metric_x, res$metric_y, paired=TRUE,conf.level = 0.99)#negligable;small;medium ou large
median(res$metric_x,na.rm = TRUE)
median(res$metric_y,na.rm = TRUE)