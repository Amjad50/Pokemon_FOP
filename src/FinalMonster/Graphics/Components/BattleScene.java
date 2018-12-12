package FinalMonster.Graphics.Components;

import FinalMonster.Graphics.Constrains;
import FinalMonster.Graphics.Storage.ImageDB;
import FinalMonster.Parser.Move;
import FinalMonster.Parser.Pokemon;
import FinalMonster.Utils.BattleLogic;
import FinalMonster.Utils.Callback;
import FinalMonster.Utils.SwitchMode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static FinalMonster.Utils.BattleLogic.getOpponentMove;

public class BattleScene extends StackPane {

	private enum Who {
		PLAYER,
		OPPONENT
	}

	private final double MARGIN_SIZE = 20,
			STATUS_WIDTH = 250,
			STATUS_HEIGHT = 50,
			BOTTOM_HEIGHT = 100;
	@FXML
	private Pane status_pane;
	@FXML
	private Pane normal_pane;

	@FXML
	private Pane root;
	@FXML
	private StatusBar status_opponent;
	@FXML
	private StatusBar status_player;

	@FXML
	private BottomBar bottomBar;

	private String playerName;
	private ArrayList<Pokemon> playerPokemons;
	private Pokemon playerCurrent;
	private String opponentName;
	private ArrayList<Pokemon> opponentPokemons;
	private Pokemon opponentCurrent;

	private int playerAcc;
	private int opponentAcc;

	private Move opponentHoldMove;

	private Who playing;

	private Queue<String> sentences;
	private Queue<AttackDefenceRequest> attackDefenceQueue;


	public BattleScene(String playername, ArrayList<Pokemon> playerpokemons, String opponentname, ArrayList<Pokemon> opponentpokemons) throws IOException {
		playerName = playername;
		playerPokemons = playerpokemons;
		opponentName = opponentname;
		opponentPokemons = opponentpokemons;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("battle_scene.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		loader.load();

		sentences = new LinkedList<>();
		attackDefenceQueue = new LinkedList<>();

		initHandler();
		switchPokemon(Who.PLAYER, SwitchMode.START, 0, r -> {
		});
		switchPokemon(Who.OPPONENT, SwitchMode.START, 0, r -> {
		});
		loop();
	}


	public BattleScene(String playername, Pokemon[] playerpokemons, String opponentname, Pokemon[] opponentpokemons) throws IOException {
		this(playername, new ArrayList<>(Arrays.asList(playerpokemons)), opponentname, new ArrayList<>(Arrays.asList(opponentpokemons)));
	}

	public BattleScene() throws IOException {
		this("", new ArrayList<>(), "", new ArrayList<>());
	}

	@FXML
	private void initialize() {
		initSizes();
//		status_player.layout();
		this.layout();
		double status_playerY = Constrains.ROOT_HEIGHT - MARGIN_SIZE - STATUS_HEIGHT - BOTTOM_HEIGHT - 20,
				status_playerX = Constrains.ROOT_WIDTH - MARGIN_SIZE - status_player.getWidth() - 20,
				status_opponentY = MARGIN_SIZE,
				status_opponentX = MARGIN_SIZE;

		status_opponent.setLayoutX(status_opponentX);
		status_opponent.setLayoutY(status_opponentY);
		status_player.setLayoutX(status_playerX);
		status_player.setLayoutY(status_playerY);
		bottomBar.setLayoutY(Constrains.ROOT_HEIGHT - BOTTOM_HEIGHT);

		root.setBackground(new Background(new BackgroundImage(
				new Image(ImageDB.BG[0]),
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT,
				new BackgroundSize(Constrains.ROOT_WIDTH, Constrains.ROOT_HEIGHT, false, false, false, false)
		)));
	}

	private void initSizes() {

		root.setPrefHeight(Constrains.ROOT_HEIGHT);
		root.setPrefWidth(Constrains.ROOT_WIDTH);
		status_pane.setPrefHeight(Constrains.ROOT_HEIGHT);
		status_pane.setPrefWidth(Constrains.ROOT_WIDTH);

	}

	public void addChild(Node node) {
		this.normal_pane.getChildren().add(node);
	}


	private void initHandler() {
		this.bottomBar.setFightAction(event -> fight());
		this.bottomBar.setRunAction(event -> run());
		this.bottomBar.setPokemonAction(event -> changePokemon());
	}

	private void loop() {
		while ( attackDefenceQueue.peek() == null )
			startBattle();

		runBattle();
	}

	private void runBattle() {
		if ( attackDefenceQueue.poll().who == Who.OPPONENT ) {
			if ( opponentCurrent.getHp() == 0 ) {
				switchPokemon(Who.OPPONENT, SwitchMode.DIED, 0, result -> {
					if ( result ) {
						if ( playerCurrent.getHp() == 0 ) {
							switchPokemon(Who.PLAYER, SwitchMode.DIED, 0, result2 -> {
								if ( result2 ) {
									opponentHoldMove = BattleLogic.getOpponentMove(opponentCurrent);
									speak(String.format("The foe's %s used %s", opponentCurrent.getName(), opponentHoldMove.getName()), () -> {
										attack(new AttackDefence(opponentHoldMove, opponentCurrent.getAttack(), playerCurrent), Who.OPPONENT, this::loop);
									});
								} else {
									win(Who.OPPONENT);
								}
							});
						} else {
							opponentHoldMove = BattleLogic.getOpponentMove(opponentCurrent);
							speak(String.format("The foe's %s used %s", opponentCurrent.getName(), opponentHoldMove.getName()), () -> {
								attack(new AttackDefence(opponentHoldMove, opponentCurrent.getAttack(), playerCurrent), Who.OPPONENT, this::loop);
							});
						}
					} else {
						win(Who.PLAYER);
					}
				});
			} else {
				opponentHoldMove = BattleLogic.getOpponentMove(opponentCurrent);
				speak(String.format("The foe's %s used %s", opponentCurrent.getName(), opponentHoldMove.getName()), () -> {
					attack(new AttackDefence(opponentHoldMove, opponentCurrent.getAttack(), playerCurrent), Who.OPPONENT, this::loop);
				});
			}

		} else {
			if ( playerCurrent.getHp() == 0 ) {
				switchPokemon(Who.PLAYER, SwitchMode.DIED, 0, result -> {
					if ( result ) {
						if ( opponentCurrent.getHp() == 0 ) {
							switchPokemon(Who.OPPONENT, SwitchMode.DIED, 0, result2 -> {
								if ( result2 ) {
									showControls();
								} else {
									win(Who.PLAYER);
								}
							});
						} else {
							showControls();
						}
					} else {
						win(Who.OPPONENT);
					}
				});
			} else {
				showControls();
			}
		}
	}

	private void startBattle() {
		int playerSpeed = playerCurrent.getSpeed();
		int opponentSpeed = opponentCurrent.getSpeed();
		playerAcc += playerSpeed;
		opponentAcc += opponentSpeed;

		if ( playerSpeed > opponentSpeed ) {
			while ( playerAcc > 100 ) {
				playerAcc -= 100;
				attackDefenceQueue.add(new AttackDefenceRequest(Who.PLAYER));
			}
			while ( opponentAcc > 100 ) {
				opponentAcc -= 100;
				attackDefenceQueue.add(new AttackDefenceRequest(Who.OPPONENT));
			}
		} else {
			while ( opponentAcc > 100 ) {
				opponentAcc -= 100;
				attackDefenceQueue.add(new AttackDefenceRequest(Who.OPPONENT));
			}
			while ( playerAcc > 100 ) {
				playerAcc -= 100;
				attackDefenceQueue.add(new AttackDefenceRequest(Who.PLAYER));
			}
		}

	}

	private void win(Who player) {
		System.out.println("winnnnnn");
	}

	private void switchPokemon(Who who, SwitchMode mode, int i, Callback.WithArg<Boolean> callable) {
		switch ( mode ) {
			case NEXT:
				break;
			case SET:
				if ( who == Who.PLAYER ) {
					unsummonPlayer();
					if ( i < playerPokemons.size() ) {
						playerCurrent = playerPokemons.get(i);
						summonPlayer();
						callable.call(true);
					} else {
						callable.call(false);
					}
				} else {
					unsummonOpponent();
					if ( i < opponentPokemons.size() ) {
						opponentCurrent = opponentPokemons.get(i);
						summonOpponent();
						callable.call(true);
					} else {
						callable.call(false);
					}
				}
				break;
			case DIED:
				if ( who == Who.PLAYER ) {
					speak(String.format("%s has fainted!", playerCurrent.getName()), () -> {
						playerPokemons.remove(playerCurrent);
						unsummonPlayer();
						if ( playerPokemons.isEmpty() ) {
							callable.call(false);
						} else {
							playerCurrent = playerPokemons.get(0);
							summonPlayer();
							callable.call(true);
						}

					});
				} else {
					speak(String.format("%s's %s has fainted!", opponentName, opponentCurrent.getName()), () -> {
						opponentPokemons.remove(opponentCurrent);
						unsummonOpponent();
						if ( opponentPokemons.isEmpty() ) {
							callable.call(false);
						} else {
							opponentCurrent = opponentPokemons.get(0);
							summonOpponent();
							callable.call(true);
						}

					});
				}
				break;
			case START:
				if ( who == Who.PLAYER ) {
					playerCurrent = playerPokemons.get(0);
					summonPlayer();
				} else {
					opponentCurrent = opponentPokemons.get(0);
					summonOpponent();
				}
				break;
		}
	}

	private void unsummonOpponent() {
		System.out.println("unsummonOpponent");
	}

	private void unsummonPlayer() {
		System.out.println("unsummonPlayer");
	}

	private void summonOpponent() {
		System.out.println("summonOpponent");
		status_opponent.setFull_hp(opponentCurrent.getFullHp());
		status_opponent.setHealth(opponentCurrent.getHp());
		status_opponent.setVisible(true);
		status_opponent.setName(opponentCurrent.getName());
	}

	private void summonPlayer() {
		System.out.println("summonPlayer");
		status_player.setFull_hp(playerCurrent.getFullHp());
		status_player.setHealth(playerCurrent.getHp());
		status_player.setVisible(true);
		status_player.setName(playerCurrent.getName());
	}

	private void changePokemon() {
		//todo ; implement this
	}

	private void run() {
		//todo: implement this after map(exit from this scene)
	}

	private void fight() {
		BottomBar.MoveHandler[] handlers = new BottomBar.MoveHandler[4];
		Pokemon attacking, defending;
		attacking = playerCurrent;
		defending = opponentCurrent;
		Move[] moves = attacking.getMoves();
		assert moves.length == 4;

		for ( int i = 0; i < 4; i++ ) {
			final AttackDefence attackDefence = new AttackDefence(moves[i], attacking.getAttack(), defending);
			handlers[i] = new BottomBar.MoveHandler(moves[i].getName(), () -> {
				this.playerAttack(attackDefence, this::loop);
				this.bottomBar.setMovesVisibile(false);
			});
		}


		this.bottomBar.setMoves(handlers);
		this.bottomBar.setMovesVisibile(true);
		hideControls();
	}

	private void playerAttack(AttackDefence attackDefence, Callback callable) {
		speak(String.format("%s used %s", playerCurrent.getName(), attackDefence.attackingMove.getName()), () -> {
			attack(attackDefence, Who.PLAYER);
			callable.call();
		});

	}

	private void attack(AttackDefence attackDefence, Who attacking) {
		this.attack(attackDefence, attacking, () -> {
		});
	}

	private void attack(AttackDefence attackDefence, Who attacking, Callback callable) {
		BattleLogic.AttackResult result = BattleLogic.attack(attackDefence.defendingPokemon, attackDefence.attackingPower, attackDefence.attackingMove);
		if ( attacking == Who.PLAYER ) {
			switch ( result.getStatus() ) {
				case SUPER_EFFECTIVE:
					System.out.println("It's super effective!");
					break;
				case MISS:
					System.out.println("Missed!");
					break;
				case NOT_VERY_EFFECTIVE:
					System.out.println("It's not very effective...");
					break;
				case NOT_EFFECTIVE:
					System.out.println("It's not effective at all!");
			}
			opponentCurrent.setHp(result.getNewHP());
			status_opponent.setHealth(result.getNewHP(), callable);
		} else {
			playerCurrent.setHp(result.getNewHP());
			status_player.setHealth(result.getNewHP(), callable);
		}
	}

	private boolean speak(String s, Callback callable) {
		if ( s == null || callable == null )
			return false;
		return this.bottomBar.setText(s, callable);
	}

	private void showControls() {
		this.bottomBar.setControlsVisibile(true);
	}

	private void hideControls() {
		this.bottomBar.setControlsVisibile(false);
	}

	private class AttackDefenceRequest {
		Who who;

		private AttackDefenceRequest(Who who) {
			this.who = who;
		}
	}

	private class AttackDefence {
		private Move attackingMove;
		private int attackingPower;
		private Pokemon defendingPokemon;

		AttackDefence(Move attackingMove, int attackingPower, Pokemon defendingPokemon) {
			this.attackingMove = attackingMove;
			this.attackingPower = attackingPower;
			this.defendingPokemon = defendingPokemon;
		}
	}
}
