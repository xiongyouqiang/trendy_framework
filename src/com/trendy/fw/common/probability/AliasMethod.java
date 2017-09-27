package com.trendy.fw.common.probability;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class AliasMethod {

    private final Random random;
 
    private final int[] alias;
    private final double[] probability;
 
    public AliasMethod(TreeMap<String, Double> initMap) {
    	this(new ArrayList<Double>(initMap.values()), new Random());
    }
    
    public AliasMethod(List<Double> probabilities) {
        this(probabilities, new Random());
    }
 
    public AliasMethod(List<Double> probabilities, Random random) {
        if (probabilities == null || random == null)
            throw new NullPointerException();
        if (probabilities.size() == 0)
            throw new IllegalArgumentException("Probability vector must be nonempty.");
 
        int probSize = probabilities.size();
        
        probability = new double[probSize];
        alias = new int[probSize];
 
        this.random = random;
 
        final double average = 1.0 / probSize;
 
        probabilities = new ArrayList<Double>(probabilities);
 
        Deque<Integer> small = new ArrayDeque<Integer>();
        Deque<Integer> large = new ArrayDeque<Integer>();
 
        for (int i = 0; i < probSize; ++i) {
            if (probabilities.get(i) >= average)
                large.add(i);
            else
                small.add(i);
        }
        //System.out.println("large: " + JsonKit.toJson(large) + " small : "+ JsonKit.toJson(small));
        
        while (!small.isEmpty() && !large.isEmpty()) {
            int less = small.removeLast();
            int more = large.removeLast();
 
            //System.out.println("less : "+ less + " more : " + more );
            
            probability[less] = probabilities.get(less) * probSize;
            alias[less] = more;
 
            probabilities.set(more, (probabilities.get(more) + probabilities.get(less)) - average);
            //System.out.println(JsonKit.toJson(probabilities));
            
            if (probabilities.get(more) >= 1.0 / probSize)
                large.add(more);
            else
                small.add(more);
        }
 
        while (!small.isEmpty())
            probability[small.removeLast()] = 1.0;
        while (!large.isEmpty())
            probability[large.removeLast()] = 1.0;
        

        //System.out.println(JsonKit.toJson(alias) + "||" + JsonKit.toJson(probability));
    }
 
    public int next() {
        int column = random.nextInt(probability.length);
 
        boolean coinToss = random.nextDouble() < probability[column];
 
        return coinToss ? column : alias[column];
    }
}
