from abc import ABC # Abstract Base Class
import numpy as np
import math


# strategija gen brojeva
class GenerirajBrojeve(ABC):
    def generiraj(self):
        pass

# konkretne strategije generiranja brojeva
class GenerirajBrojeveSlijedno(GenerirajBrojeve):
    def generiraj(self, **kwargs):
        start = kwargs['start']
        stop = kwargs['stop']
        step = kwargs['step']
        gen_brojevi = []

        for i in range(start, stop, step):
            gen_brojevi.append(i)

        return gen_brojevi
    
class GenerirajBrojeveSlucajno(GenerirajBrojeve):
    def generiraj(self, **kwargs):
        mean = kwargs['mean']
        stddev = kwargs['stddev']
        n = kwargs['n']
        gen_brojevi = []

        x = np.random.normal(loc=mean, scale = stddev, size=n).astype(int)
        gen_brojevi = np.sort(x)

        return gen_brojevi

class GenerirajBrojeveFibonacci(GenerirajBrojeve):
    def generiraj(self, **kwargs):
        n = kwargs['n']
        f1 = 0
        f2 = 1
        gen_brojevi = []

        if (n < 1): 
            return
        
        gen_brojevi.append(f1)

        for x in range(1, n):
            gen_brojevi.append(f2)
            slj = f1 + f2
            f1 = f2
            f2 = slj
        
        return gen_brojevi



# strategija odredivanja distirbucije
class OdrediPtiPercentil(ABC):
    def odredi(self):
        pass

# konkretne startegije odredivanja percentila 
class OdrediPtiPercentilNajblizi(OdrediPtiPercentil):
    def odredi(self, p, list_gen_brojeva):
        N = len(list_gen_brojeva)
        n_p = math.ceil(p*N / 100 + 0.5)
        
        return list_gen_brojeva[n_p-1]
    
class OdrediPtiPercentilInterpolirani(OdrediPtiPercentil):
    def odredi(self, p, list_gen_brojeva):
        N = len(list_gen_brojeva)

        v_1 = list_gen_brojeva[0]
        v_N = list_gen_brojeva[N-1]

        p_v_1 = 100 * (1-0.5) / N
        if (p < p_v_1):
            return v_1
        
        p_v_N = 100 * (N-0.5) / N
        if (p > p_v_N): 
            return v_N

        for i in range(1, N):
            p_v_i = 100 * (i-0.5) / N
            p_v_i_1 = 100 * ((i+1)-0.5) / N

            if (p_v_i < p < p_v_i_1):
                v_i = list_gen_brojeva[i-1]
                v_i_1 = list_gen_brojeva[i]

                v_p = v_i + N*(p-p_v_i) * (v_i_1 - v_i) / 100
                break

        return int(v_p)



# context
class DistributionTester(): 
    def __init__(self, strategy_generiraj: GenerirajBrojeve, strategy_odredi: OdrediPtiPercentil) -> None:
        self._strategy_generiraj = strategy_generiraj
        self._strategy_odredi = strategy_odredi

    def generiraj_brojeve(self, **kwargs) -> list:
        result = self._strategy_generiraj.generiraj(**kwargs)
        return result
    
    def odredi_percentil(self, p, list_gen_brojeva) -> int:
        result = self._strategy_odredi.odredi(p, list_gen_brojeva)
        return result



# main
if __name__ == "__main__":
    context1 = DistributionTester(GenerirajBrojeveSlijedno(), OdrediPtiPercentilNajblizi())
    result1_1 = context1.generiraj_brojeve(start=1,stop=10,step=2)
    print('Slijedno genereirani brojevi: ', result1_1)
    for i in range(10, 100, 10):
        result1_2 = context1.odredi_percentil(i, result1_1)
        print('{}-ti percentil u {} je: {}'.format(i, result1_1, result1_2))
    print()

    context2 = DistributionTester(GenerirajBrojeveSlucajno(), OdrediPtiPercentilInterpolirani())
    result2_1 = context2.generiraj_brojeve(mean=5,stddev=2,n=10)
    print('Slucajno genereirani brojevi po normalnoj distribuciji: ', result2_1)
    for i in range(10, 100, 10):
        result2_2 = context2.odredi_percentil(i, result2_1)
        print('{}-ti percentil u {} je: {}'.format(i, result2_1, result2_2))
    print()

    context3 = DistributionTester(GenerirajBrojeveFibonacci(), OdrediPtiPercentilNajblizi())
    result3_1 = context3.generiraj_brojeve(n=7)
    print('Slijedno genereirani brojevi (Fibonacci): ', result3_1)
    for i in range(10, 100, 10):
        result3_2 = context3.odredi_percentil(i, result3_1)
        print('{}-ti percentil u {} je: {}'.format(i, result3_1, result3_2))
    print()