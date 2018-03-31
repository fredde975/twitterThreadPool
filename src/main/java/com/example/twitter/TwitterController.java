package com.example.twitter;

import com.example.twitter.exceptions.TagInputException;
import com.example.twitter.models.ErrorResponse;
import com.example.twitter.models.WordItem;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import twitter4j.TwitterException;

import java.util.List;

@RestController
public class TwitterController {
    @Autowired
    private TwitterService twitterService;
    private static final org.apache.log4j.Logger LOG = Logger.getLogger(TwitterController.class);

    @RequestMapping(value = "sortedTweets", method = RequestMethod.GET)
    public List<WordItem> getTwitterTag(@RequestParam("tag") String tag) throws TagInputException, TwitterException {
        String hashTag = createHashtagFromQueryString(tag);
        List<WordItem> words = twitterService.handleRequest(hashTag);

        return words;
    }

    private String createHashtagFromQueryString(String tag) throws TagInputException {
        if (tag != null && tag.length() > 0 && !tag.startsWith("#")) {
            LOG.info("Recieved tag: " + tag);
            return "#" + tag;
        }

        throw new TagInputException("You must have the query string 'twitterTag=tagname' set in the url, i.e. don't use '#' in the query");
    }


    @ExceptionHandler({TagInputException.class, TwitterException.class})
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        HttpStatus status = null;

        if (ex instanceof TagInputException) {
           status =  HttpStatus.UNPROCESSABLE_ENTITY;
        } else if (ex instanceof TwitterException) {
           status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return  new ResponseEntity<>(error, status);

    }
}
