package tleaf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import tleaf.data.TweetRepository;
import tleaf.entity.Tweet;
import tleaf.entity.TweetForm;
import tleaf.exception.DuplicateTweetException;
import tleaf.exception.TweetNotFoundException;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/tweets")
public class TweetController {
    //private static final String MAX_LONG_AS_STRING = "9223372036854775807";
    private TweetRepository repository;

    @Autowired
    public TweetController(TweetRepository repository){
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Tweet> tweets(@RequestParam(value = "count", defaultValue = "20") int count){
        //@RequestParam(value = "max", defaultValue = MAX_LONG_AS_STRING) long max,
        return repository.findRecentTweets(count);
    }

    @RequestMapping(value = "/{tweetId}", method = RequestMethod.GET)
    public String tweet(@PathVariable("tweetId") long tweetId, Model model){
        Tweet tweet = repository.findOne(tweetId);
        if ( tweet == null ) {
            throw new TweetNotFoundException();
        }
        model.addAttribute(tweet);
        return "tweet";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String saveTweet(@Valid TweetForm form, Model model) {//throws Exception{
        try {
            repository.save(new Tweet(null, form.getMessage(), new Date()));
            return "redirect:/tweets";
        } catch (DuplicateTweetException e) {
            return "error/duplicate";
        }
    }

}
