/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author ACER
 */
public class GeneticAlgorithm {
    List<KnapSack> population;
    List<Item> listItem;
    int maxWeight;
    int populationSize = 100;
    int generations = 100;
    double mutationRate = 0.01;
    
    public  GeneticAlgorithm(List<Item> listItem, int maxWeight){
        this.listItem = listItem;
        this.maxWeight = maxWeight;
        this.population = new ArrayList<>();
    }
    
    // Tạo quần thể ban đầu
    public void initializePopulation(){
        for(int i = 0; i< populationSize;i++){
            KnapSack knapSack = new KnapSack(listItem,maxWeight);
            knapSack.randomizeGenes();
            population.add(knapSack);
        }
    }
    
    // Chọn cá thể vượt trội nhất
    public KnapSack selectBestParent() {
        // Roulette Wheel Selection
        int totalFitness = population.stream().mapToInt(k -> k.fitness).sum();
        Random rand = new Random();
        int randomFitness = rand.nextInt(totalFitness);
        int cumulativeFitness = 0;

        for (KnapSack knapsack : population) {
            cumulativeFitness += knapsack.fitness;
            if (cumulativeFitness >= randomFitness) {
                return knapsack;
            }
        }
        return population.get(0); // Fallback
    }
    // Lai ghép cá thể con
    public KnapSack crossover(KnapSack parent1, KnapSack parent2) {
        KnapSack offspring = new KnapSack(listItem, maxWeight);
        Random rand = new Random();
        int crossoverPoint = rand.nextInt(parent1.genes.length);

        for (int i = 0; i < parent1.genes.length; i++) {
            offspring.genes[i] = (i < crossoverPoint) ? parent1.genes[i] : parent2.genes[i];
        }

        offspring.calculateFitness();
        return offspring;
    }
    // Đột biến một cá thể
    public void mutate(KnapSack knapsack) {
        Random rand = new Random();
        for (int i = 0; i < knapsack.genes.length; i++) {
            if (rand.nextDouble() < mutationRate) {
                knapsack.genes[i] = !knapsack.genes[i];
            }
        }
        knapsack.calculateFitness();
    }

    public KnapSack run() {
        initializePopulation();

        for (int generation = 0; generation < generations; generation++) {
            List<KnapSack> newPopulation = new ArrayList<>();

            for (int i = 0; i < populationSize; i++) {
                KnapSack parent1 = selectBestParent();
                KnapSack parent2 = selectBestParent();
                KnapSack offspring = crossover(parent1, parent2);
                mutate(offspring);
                newPopulation.add(offspring);
            }

            population = newPopulation;
        }

        return population.stream().max((k1, k2) -> Integer.compare(k1.fitness, k2.fitness)).orElse(null);
    }
}
