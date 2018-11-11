package pokemon;

import pokemon.Graphics.PokemonApplication;

public class testMain {
	public static void main(String[] args) {
		PokemonApplication app = new PokemonApplication();

		app.run();
		app.addImage("http://www.pokestadium.com/sprites/xy/charmander.gif", 500, 500);
	}
}
