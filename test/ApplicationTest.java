import static org.fest.assertions.Assertions.assertThat;
import models.Book;
import models.BookInfo;

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
		BookInfo bookinf = new BookInfo();
				bookinf.findByCode(book.isbn_code);
		assertThat("Play Framework 2徹底入門 JavaではじめるアジャイルWeb開発").isEqualTo(bookinf.bookName);
		assertThat("掌田 津耶乃").isEqualTo(bookinf.author);
		assertThat("翔泳社").isEqualTo(bookinf.publisher);
	}

}
