library(lsr)
sold<-table(Dataset$refactoring_x,Dataset$refactoring_y)
chisq.test(sold, simulate.p.value = TRUE)
cramersV(sold, simulate.p.value = TRUE)