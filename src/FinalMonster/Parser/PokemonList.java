/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FinalMonster.Parser;

import java.util.Random;

/**
 *
 * @author User
 */
public class PokemonList {

    private static String[] PokemonNames = {"Bulbasaur", "Charmander", "Squirtle", "Chikorita", "Cyndaquil", "Totodile", "Treecko", "Torchihc", "Mudkip", "Turtwig", "Chimchar", "Piplup", "Snivy", "Tepig", "Oshawott", "Ratata", "Pidgey", "Sentret", "Hoothoot", "Zigzagoon", "Poochyena", "Bidoof", "Starly", "Patrat", "Pidove", "Ivysaur", "Charmeleon", "Wartortle", "Bayleef", "Quilava", "Croconaw", "Grovyle", "Combusken", "Marshtomp", "Grotle", "Monferno", "Prinplup", "Raticate", "Pidgeotto", "Furret", "Noctowl", "Linoone", "Mightyena", "Bibarel", "Staravia", "Watchog", "Tranquill", "Venusaur", "Charizard", "Blastoise", "Meganium", "Typhlosion", "Feraligatr", "Sceptile", "Blaziken", "Torterra", "Infernape", "Empoleon", "Serperior", "Emboar", "Samurott", "Pidgeot", "Staraptor", "Unfezant", "Dragonite", "Metagross", "Salamence", "Garchomp", "Hydreigon", "Mewtwo", "Lugia", "Ho-oh", "Rayquaza", "Groudon", "Kyogre", "Deoxys", "Dialga", "Palkia", "Giratina", "Zekrom", "Reshiram", "Kyurem", "Genesect", "Arceus"};
    private static Pokemon[] pokemons;

    private static String[] Player = new String[3];
    private static Pokemon[] Easy;
    private static Pokemon[] Normal;
    private static Pokemon[] Hard;
    private static Pokemon[] Extreme;
    private static Pokemon[] Legend;
    private static Random r = new Random();

    static {
        pokemons = Parser.scan();
        System.out.println(pokemons.length);
    }
    
    private PokemonList(){}

    private void Player() {
        for (int i = 0; i < Player.length; i++) {
            Player[i] = PokemonNames[r.nextInt(25)];
            for (int j = 0; j < i; j++) {
                if (Player[j] == Player[i]) {
                    i--;
                    break;
                }
            }

        }
        for (int i = 0; i < Player.length; i++) {
            System.out.println("Player Choose: " + Player[i]);
        }
    }

    public static Pokemon[] Easy() {
        if (Easy == null) {
            Easy = new Pokemon[3];
            for (int i = 0; i < Easy.length; i++) {
                Easy[i] = pokemons[r.nextInt(88)];
                for (int j = 0; j < i; j++) {
                    if (Easy[j].equals(Easy[i])) {
                        i--;
                        break;
                    }
                }
            }
        }
        return Easy;
    }

    public Pokemon[] Normal() {
        if (Normal == null) {
            Normal = new Pokemon[3];
            for (int i = 0; i < Normal.length; i++) {
                Normal[i] = pokemons[r.nextInt(14) + 21];
                for (int j = 0; j < i; j++) {
                    if (Normal[j].equals(Normal[i])) {
                        i--;
                        break;
                    }
                }
            }
        }
        return Normal;
    }

    public Pokemon[] Hard() {
        if (Hard == null) {
            Hard = new Pokemon[3];
            for (int i = 0; i < Hard.length; i++) {
                Hard[i] = pokemons[r.nextInt(5) + 35];
                for (int j = 0; j < i; j++) {
                    if (Hard[j].equals(Hard[i])) {
                        i--;
                        break;
                    }
                }
            }
        }
        return Hard;
    }

    public Pokemon[] Legend() {
        if (Legend == null) {
            Legend = new Pokemon[3];
            for (int i = 0; i < Legend.length; i++) {
                Legend[i] = pokemons[r.nextInt(15) + 40];
                for (int j = 0; j < i; j++) {
                    if (Legend[j].equals(Legend[i])) {
                        i--;
                        break;
                    }
                }
            }
        }
        return Legend;
    }

}
