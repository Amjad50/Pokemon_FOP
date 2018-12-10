/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FinalMonster.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class Parser {
    
    private static final int POKEMON_ARR_SIZE= 88;
    
    public static Pokemon[] scan(){
        try {
            Scanner s = new Scanner(new FileInputStream(Parser.class.getResource("pokemon.txt").getPath()));
            Pokemon [] pokemon = new Pokemon[POKEMON_ARR_SIZE];
            for (int j =0;j<POKEMON_ARR_SIZE;j++){
            String name = s.nextLine();
            String type = s.nextLine();
            int hp = s.nextInt();
            int attack = s.nextInt();
            int defense = s.nextInt();
            int speed = s.nextInt();
            s.nextLine();
            
            Move[] moves = new Move[4];
            
            for (int i = 0; i<4;i++){
                String movename = s.nextLine();
                String movetype = s.nextLine();
                int power = s.nextInt();
                int accuracy = s.nextInt();
                
                s.nextLine();
                moves[i]= new Move(movename,movetype,power,accuracy);
            }
            if(s.hasNext())
                s.nextLine();
            pokemon [j]= new Pokemon(name,type,hp,attack,defense,speed,moves);
            }
            
            return pokemon;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            return new Pokemon[0];
        }
    
}
}
