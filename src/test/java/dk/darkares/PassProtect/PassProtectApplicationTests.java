package dk.darkares.PassProtect;

import dk.darkares.PassProtect.models.User;
import dk.darkares.PassProtect.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PassProtectApplicationTests {

	@Autowired
	private UserService userService;

	@Test
	public void contextLoads() {
	}


	@Test
	public void whenFindByName_thenReturnEmployee() {
		// given
		User alex = new User();
		alex.setName("Alex");
		alex.setPassword("secret");
		userService.createUser(alex);

		// when
		User found = userService.getUserByName(alex.getName());

		// then
		assertThat(found.getName())
				.isEqualTo(alex.getName());
	}
}
