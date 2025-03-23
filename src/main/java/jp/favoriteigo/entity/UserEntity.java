package jp.favoriteigo.entity;

import org.seasar.doma.*;

@Entity
@Table(name = "users")
public class UserEntity {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id = 0;

    @Column(name = "name")
    String name = null;

    @Column(name = "password")
    String password = null;

    @Column(name = "chess_ability")
    String chessAbility = null;

    @Column(name = "chess_ability_kbn")
    String chessAbilityKbn = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getChessAbility() {
        return chessAbility;
    }

    public void setChessAbility(String chessAbility) {
        this.chessAbility = chessAbility;
    }

    public String getChessAbilityKbn() {
        return chessAbilityKbn;
    }

    public void setChessAbilityKbn(String chessAbilityKbn) {
        this.chessAbilityKbn = chessAbilityKbn;
    }
}
