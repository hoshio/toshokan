import static org.fest.assertions.Assertions.assertThat;

import models.Book;

import org.junit.Test;

import controllers.ItemLookup;


/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest {
	
	@Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }
	
	@Test
	public void setBookInfTest() {
		Book book = new Book();
		book.isbn_code = "9784798133928";
		ItemLookup.setBookInf(book);
		assertThat("Play Framework 2徹底入門 JavaではじめるアジャイルWeb開発").isEqualTo(book.book_name);
		assertThat("掌田 津耶乃").isEqualTo(book.author);
		assertThat("翔泳社").isEqualTo(book.publisher);
	}

}
