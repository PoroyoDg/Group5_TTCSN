/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ga;

import java.util.List;
import java.util.Random;

/**
 *
 * @author ACER
 */
public class KnapSack {
    List<Item> listItem;
    boolean[] genes;
    int fitness;
    int maxWeight;

    public List<Item> getListItem() {
        return listItem;
    }

    public void setListItem(List<Item> listItem) {
        this.listItem = listItem;
    }

    public boolean[] getGenes() {
        return genes;
    }

    public void setGenes(boolean[] genes) {
        this.genes = genes;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public KnapSack() {
    }
    
    
    public KnapSack(List<Item> listitem , int maxWeight){
        this.listItem = listitem;
        this.genes = new boolean[listitem.size()];
        this.maxWeight = maxWeight;
        this.fitness = 0;
    }
    // Hàm tính toán fitness 
    public void calculateFitness(){
        int totalWeight = 0;
        int totalValue = 0;
        
        for (int i = 0; i < genes.length; i++){
            if(genes[i]){
                totalWeight += listItem.get(i).weight;
                totalValue += listItem.get(i).value;
            }
        }
        if (totalWeight > maxWeight){
            this.fitness = 0;
        }
        else{
            this.fitness = totalValue;
        }
    }
    // Hàm random chọn các túi
    public void randomizeGenes(){
        Random randomItem = new Random();
        for (int i = 0; i < genes.length; i++){
            genes[i] = randomItem.nextBoolean();
        }
        calculateFitness();
    }
}
