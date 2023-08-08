import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

@Builder
@Getter
@Setter
public class User {
    private String email;
    private String password;
    private String name;
    public static User generateRandomUser(){
        String email = RandomStringUtils.randomAlphabetic(6) + "@six.org";
        String password = RandomStringUtils.randomAlphabetic(6);
        String name = RandomStringUtils.randomAlphabetic(6);
        return new User(email, password, name);
    }
}
