package FinalMonster.Graphics.Components;

import FinalMonster.Graphics.Constrains;
import FinalMonster.Graphics.Storage.ImageDB;
import FinalMonster.Parser.Move;
import FinalMonster.Parser.Pokemon;
import FinalMonster.Utils.BattleLogic;
import FinalMonster.Utils.Callback;
import FinalMonster.Utils.SwitchPokemon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static java.util.stream.Collectors.toList;

public class BattleScene extends StackPane {

	public enum Who {
		PLAYER,
		OPPONENT
	}

	public static final int OPPONENT_X_BOTTOM = 500,
			OPPONENT_Y_BOTTOM = 350,
			PLAYER_X_CENTER = 250,
			PLAYER_Y_CENTER = 400;

	private final double MARGIN_SIZE = 20,
			STATUS_WIDTH = 300,
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

	@FXML
	private ImageView player_image;
	@FXML
	private ColorAdjust playerColorAdjust;
	@FXML
	private ImageView opponent_image;
	@FXML
	private ColorAdjust opponentColorAdjust;

	private String playerName;
	private ArrayList<Pokemon> playerPokemons;
	private Pokemon playerCurrent;
	private String opponentName;
	private ArrayList<Pokemon> opponentPokemons;
	private Pokemon opponentCurrent;

	private int playerAcc;
	private int opponentAcc;

	private Move opponentHoldMove;

	private Queue<AttackDefenceRequest> attackDefenceQueue;


	public BattleScene(String playername, ArrayList<Pokemon> playerpokemons, String opponentname, ArrayList<Pokemon> opponentpokemons) throws IOException {
		playerName = playername;
		playerPokemons = (ArrayList<Pokemon>) playerpokemons.stream().map(Pokemon::clone).collect(toList());
		opponentName = opponentname;
		opponentPokemons = (ArrayList<Pokemon>) opponentpokemons.stream().map(Pokemon::clone).collect(toList()); ;
		System.out.println(playerPokemons);
		System.out.println(opponentPokemons);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("battle_scene.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		loader.load();

		attackDefenceQueue = new LinkedList<>();

		initHandler();
		switchPokemon(Who.PLAYER, SwitchPokemon.SwitchMode.START, 0, r -> {
			switchPokemon(Who.OPPONENT, SwitchPokemon.SwitchMode.START, 0, r2 -> {
				loop();
			});
		});
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
				status_playerX = Constrains.ROOT_WIDTH - MARGIN_SIZE - STATUS_WIDTH,
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
				switchPokemon(Who.OPPONENT, SwitchPokemon.SwitchMode.DIED, 0, result -> {
					if ( result ) {
						opponentHoldMove = BattleLogic.getOpponentMove(opponentCurrent);
						speak(String.format("The foe's %s used %s", opponentCurrent.getName(), opponentHoldMove.getName()), () -> {
							attack(new AttackDefence(opponentHoldMove, opponentCurrent.getAttack(), playerCurrent), Who.OPPONENT, () -> {
								if ( playerCurrent.getHp() == 0 ) {
									switchPokemon(Who.PLAYER, SwitchPokemon.SwitchMode.DIED, 0, result2 -> {
										if ( !result2 ) {
											win(Who.OPPONENT);
										} else {
											loop();
										}
									});
								} else {
									loop();
								}
							});
						});
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
				switchPokemon(Who.PLAYER, SwitchPokemon.SwitchMode.DIED, 0, result -> {
					if ( result ) {
						if ( opponentCurrent.getHp() == 0 ) {
							switchPokemon(Who.OPPONENT, SwitchPokemon.SwitchMode.DIED, 0, result2 -> {
								if ( result2 ) {
									speak(String.format("What will %s do?", playerCurrent.getName()), this::showControls);
								} else {
									win(Who.PLAYER);
								}
							});
						} else {
							speak(String.format("What will %s do?", playerCurrent.getName()), this::showControls);
						}
					} else {
						win(Who.OPPONENT);
					}
				});
			} else {
				speak(String.format("What will %s do?", playerCurrent.getName()), this::showControls);
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

	private void switchPokemon(Who who, SwitchPokemon.SwitchMode mode, Pokemon pokemon, Callback.WithArg<Boolean> callable) {
		switch ( mode ) {
			case SET:
				if ( who == Who.PLAYER ) {
					unsummonPlayer(() -> {
						playerCurrent = pokemon;
						summonPlayer(() -> callable.call(true));
					});
				} else {
					unsummonOpponent(() -> {
						opponentCurrent = pokemon;
						summonOpponent(() -> callable.call(true));

					});
				}
				break;
			case DIED:
				if ( who == Who.PLAYER ) {
					speak(String.format("%s has fainted!", playerCurrent.getName()), () -> {
						playerPokemons.remove(playerCurrent);
						unsummonPlayer(() -> {
							if ( playerPokemons.isEmpty() ) {
								callable.call(false);
							} else {
								playerCurrent = playerPokemons.get(0);
								summonPlayer(() -> callable.call(true));
							}
						});
					});
				} else {
					speak(String.format("%s's %s has fainted!", opponentName, opponentCurrent.getName()), () -> {
						opponentPokemons.remove(opponentCurrent);
						unsummonOpponent(() -> {
							if ( opponentPokemons.isEmpty() ) {
								callable.call(false);
							} else {
								opponentCurrent = opponentPokemons.get(0);
								summonOpponent(() -> callable.call(true));
							}
						});
					});
				}
				break;
			case START:
				if ( who == Who.PLAYER ) {
					playerCurrent = playerPokemons.get(0);
					summonPlayer(() -> callable.call(true));
				} else {
					opponentCurrent = opponentPokemons.get(0);
					summonOpponent(() -> callable.call(true));
				}
				break;
			default:
				throw new IllegalArgumentException(mode + " is not a valid type in BattleScene::switchPokemon");
		}
	}

	private void switchPokemon(Who who, SwitchPokemon.SwitchMode mode, int i, Callback.WithArg<Boolean> callable) {
		if ( who == Who.PLAYER ) {
			if ( i < playerPokemons.size() ) {
				switchPokemon(who, mode, playerPokemons.get(i), callable);
			} else {
				callable.call(false);
			}
		} else {
			if ( i < opponentPokemons.size() ) {
				switchPokemon(who, mode, opponentPokemons.get(i), callable);
			} else {
				callable.call(false);
			}
		}
	}

	private void unsummonOpponent(Callback callable) {
		System.out.println("unsummonOpponent");
		SwitchPokemon.animateSwitch(opponent_image, opponentColorAdjust, opponentCurrent.getFrontImg(), Who.OPPONENT, SwitchPokemon.SwitchMode.EXIT, callable);
	}

	private void unsummonPlayer(Callback callable) {
		System.out.println("unsummonPlayer");
		SwitchPokemon.animateSwitch(player_image, playerColorAdjust, playerCurrent.getBackImg(), Who.PLAYER, SwitchPokemon.SwitchMode.EXIT, callable);
	}

	private void summonOpponent(Callback callable) {
		System.out.println("summonOpponent");
		status_opponent.setFull_hp(opponentCurrent.getFullHp());
		status_opponent.setHealth(opponentCurrent.getHp());
		status_opponent.setVisible(true);
		status_opponent.setName(opponentCurrent.getName());
		SwitchPokemon.animateSwitch(opponent_image, opponentColorAdjust, opponentCurrent.getFrontImg(), Who.OPPONENT, SwitchPokemon.SwitchMode.ENTER, callable);
	}

	private void summonPlayer(Callback callable) {
		System.out.println("summonPlayer");
		status_player.setFull_hp(playerCurrent.getFullHp());
		status_player.setHealth(playerCurrent.getHp());
		status_player.setVisible(true);
		status_player.setName(playerCurrent.getName());
		SwitchPokemon.animateSwitch(player_image, playerColorAdjust, playerCurrent.getBackImg(), Who.PLAYER, SwitchPokemon.SwitchMode.ENTER, callable);
	}

	private void changePokemon() {
		BottomBar.MoveHandler[] handlers = new BottomBar.MoveHandler[4];

		for ( int i = 0; i < playerPokemons.size(); i++ ) {
			Pokemon p = playerPokemons.get(i);
			handlers[i] = new BottomBar.MoveHandler(p.getName(), () -> {
				this.bottomBar.setMovesVisibile(false);
				switchPokemon(Who.PLAYER, SwitchPokemon.SwitchMode.SET, p, result -> {
					if ( result ) {
						loop();
					} else {
						System.out.println("Error");
					}
				});
				( (LinkedList<AttackDefenceRequest>) attackDefenceQueue ).add(0, new AttackDefenceRequest(Who.PLAYER));
			});
		}


		for ( int i = playerPokemons.size(); i < 4; i++ ) {
			handlers[i] = new BottomBar.MoveHandler("", () -> {
			});
		}


		this.bottomBar.setMoves(handlers);
		this.bottomBar.setMovesVisibile(true);
		hideControls();
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
			attack(attackDefence, Who.PLAYER, callable);
		});

	}

	private void attack(AttackDefence attackDefence, Who attacking) {
		this.attack(attackDefence, attacking, () -> {
		});
	}

	private void attack(AttackDefence attackDefence, Who attacking, Callback callable) {
		BattleLogic.AttackResult result = BattleLogic.attack(attackDefence.defendingPokemon, attackDefence.attackingPower, attackDefence.attackingMove);
		if ( attacking == Who.PLAYER ) {
			Callback myCallable = () -> {
				opponentCurrent.setHp(result.getNewHP());
				status_opponent.setHealth(result.getNewHP(), callable);
			};
			switch ( result.getStatus() ) {
				case SUPER_EFFECTIVE:
					speak("It's super effective!", myCallable);
					break;
				case MISS:
					speak("Missed!", myCallable);
					break;
				case NOT_VERY_EFFECTIVE:
					speak("It's not very effective...", myCallable);
					break;
				case NOT_EFFECTIVE:
					speak("It's not effective at all!", myCallable);
					break;
				default:
					myCallable.call();
					break;
			}
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
