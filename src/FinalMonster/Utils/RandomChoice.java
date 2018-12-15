package FinalMonster.Utils;

import FinalMonster.Parser.Pokemon;

import java.util.*;

public class RandomChoice {

	public static Pokemon[] randomPokemons(Pokemon[] src, int length) {
		assert length <= src.length;
		Random r = new Random();
		List<Integer> used = new ArrayList<>(length);

		Pokemon[] result = new Pokemon[length];
		for ( int i = 0; i < length; i++ ) {
			result[i] = src[r.nextInt(src.length)];
			for ( int j = 0; j < i; j++ ) {
				if ( result[j].equals(result[i]) ) {
					i--;
					break;
				}
			}
		}
		return result;
	}

	public static ArrayList<Pokemon> randomPokemons(List<Pokemon> src, int length) {
		return new ArrayList<>(Arrays.asList(randomPokemons(src.toArray(new Pokemon[0]), length)));
	}
}
