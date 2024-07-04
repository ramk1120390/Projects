package com.Network.Network.DS.DesignPattren.Descorator;

public class DecoratorMain {
    public static void main(String[] args) {
        // With Combining Decorators:
        Pizza basePizza = new BasePizza();

        Pizza cheeseBurstPizza = new CheeseBurstDecorator(basePizza);

        Pizza jalapenoCheeseBurstPizza = new JalepanoDecorator(cheeseBurstPizza);
        System.out.println("Base Pizza: Description: " + basePizza.getDescription() + ", Cost: $" + basePizza.getCost());
        System.out.println("Cheese Burst Pizza: Description: " + cheeseBurstPizza.getDescription() + ", Cost: $" + cheeseBurstPizza.getCost());
        System.out.println("Jalapeno Cheese Burst Pizza: Description: " + jalapenoCheeseBurstPizza.getDescription() + ", Cost: $" + jalapenoCheeseBurstPizza.getCost());

        //Without Combining Decorators:
        Pizza combinedPizza = new JalepanoDecorator(new CheeseBurstDecorator(basePizza));
        System.out.println("Combined Pizza: Description: " + combinedPizza.getDescription() + ", Cost: $" + combinedPizza.getCost());


    }
}
