package org.completable.future.demo.api;

import com.google.common.util.concurrent.ListenableFuture;
import org.completable.future.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@RestController
@RequestMapping("api/demo")
public class DemoApi {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DemoService demoService;

    @RequestMapping(value = "/mainthread", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> testMainThread() {
        demoService.testMainThread();
        return new ResponseEntity<String>("Hello from main thread API " + Thread.currentThread().getName(), HttpStatus.OK);
    }

    @RequestMapping(value = "/async/cf", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> testAsyncCf() {
        Future<CompletableFuture<String>> result = demoService.testCompletableFutureWithExecutor();
        return new ResponseEntity<String>("Hello from test async Completable API " + Thread.currentThread().getName(), HttpStatus.OK);
    }

    @RequestMapping(value = "/async/lf", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> testAsyncLf() {
        Future<ListenableFuture<String>> result = demoService.testListenableFutureWithExecutor();
        return new ResponseEntity<String>("Hello from test async Future API " + Thread.currentThread().getName(), HttpStatus.OK);
    }

}
