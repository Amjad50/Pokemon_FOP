
package FinalMonster.Parser;

import java.io.InputStreamReader;
import java.util.Scanner;

public class Parser {

	private static final int POKEMON_ARR_SIZE = 111;

	public static Pokemon[] scan() {
		Scanner s = new Scanner(new InputStreamReader(Parser.class.getResourceAsStream("pokemon.txt")));
		Pokemon[] pokemon = new Pokemon[POKEMON_ARR_SIZE];
		for ( int j = 0; j < POKEMON_ARR_SIZE; j++ ) {
			String ID = s.nextLine();
			String name = s.nextLine();
			String type = s.nextLine();
			int hp = s.nextInt();
			int attack = s.nextInt();
			int defense = s.nextInt();
			int speed = s.nextInt();
			s.nextLine();

			Move[] moves = new Move[4];

			for ( int i = 0; i < 4; i++ ) {
				String movename = s.nextLine();
				String movetype = s.nextLine();
				int power = s.nextInt();
				int accuracy = s.nextInt();
				if ( s.hasNext() ) {
					s.nextLine();
				}
				moves[i] = new Move(movename, movetype, power, accuracy);

			}
			if ( s.hasNext() )
				s.nextLine();

			pokemon[j] = new Pokemon(ID, name, type, hp, attack, defense, speed, moves);
		}

		return pokemon;

	}
}
