package tleaf.web;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceView;
import tleaf.Tweet;
import tleaf.data.TweetRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.hamcrest.CoreMatchers.hasItems;

public class TweetControllerTest {

    @Test
    public void shouldShowDefaultTweets() throws Exception {
        // GET request "/tweets" has default values max = Long.MAX_VALUE, count = 20
        List<Tweet> expectedTweets = createTweetList(20);
        TweetRepository mockRepository = mock(TweetRepository.class);
        //when(mockRepository.findRecentTweets(Long.MAX_VALUE, 20)).thenReturn(expectedTweets);
        when(mockRepository.findRecentTweets(20)).thenReturn(expectedTweets);

        TweetController controller = new TweetController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller)
                .setSingleView(new InternalResourceView("/WEB-INF/views/tweets.html")).build();

        mockMvc.perform(get("/tweets"))
                .andExpect(view().name("tweets"))
                .andExpect(model().attributeExists("tweetList"))
                .andExpect(model().attribute("tweetList", hasItems(expectedTweets.toArray())));
    }

    @Test
    public void shouldShowPagedTweetList() throws Exception{
        List<Tweet> expectedTweets = createTweetList(50);
        TweetRepository mockRepository = mock(TweetRepository.class);
        //when(mockRepository.findRecentTweets(12345678, 50)).thenReturn(expectedTweets);
        when(mockRepository.findRecentTweets(50)).thenReturn(expectedTweets);

        TweetController controller = new TweetController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller)
                .setSingleView(new InternalResourceView("/WEB-INF/views/tweets.html")).build();

        //mockMvc.perform(get("/tweets?max=12345678&count=50"))
        mockMvc.perform(get("/tweets?count=50"))
                .andExpect(view().name("tweets"))
                .andExpect(model().attributeExists("tweetList"))
                .andExpect(model().attribute("tweetList", hasItems(expectedTweets.toArray())));
    }

    @Test
    public void shouldSaveTweet() throws Exception {
        TweetRepository mockRepository = mock(TweetRepository.class);
        TweetController controller = new TweetController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(post("/tweets").param("message", "Hello world!"))
                .andExpect(redirectedUrl("/tweets"));
        verify(mockRepository, atLeastOnce()).save(new Tweet(null, "Hello world!", new Date()));
    }

    @Test
    public void showingSingleTweet() throws Exception {
        Tweet expectedTweet = new Tweet("Hello world!", new Date());
        TweetRepository mockRepository = mock(TweetRepository.class);
        when(mockRepository.findOne(12345)).thenReturn(expectedTweet);

        TweetController controller = new TweetController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(get("/tweets/12345"))
                //.andDo(print())//for debug
                .andExpect(view().name("tweet"))
                .andExpect(model().attributeExists("tweet"))
                .andExpect(model().attribute("tweet",expectedTweet));
    }

    private List<Tweet> createTweetList(int count) {
        List<Tweet> tweets = new ArrayList<>();
        for (int i=0; i < count; i++) {
            tweets.add(new Tweet("Tweet " + i, new Date()));
        }
        return tweets;
    }
}
