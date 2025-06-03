package com.callhippo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calls")
public class CallLogController {

    @Autowired
    private CallHippoClient callHippoClient;

    @Autowired
    private CallLogService callLogService;

    // GET /api/calls/account
    @GetMapping("/account")
    public ResponseEntity<String> getAccountInfo() {
        return callHippoClient.getAccountInfo();
    }

    // POST /api/calls/account
    @PostMapping("/account")
    public ResponseEntity<String> saveCallLogs(@RequestBody List<CallLogResponse> logs) {
        logs.forEach(System.out::println); // For now, just print to console
        return ResponseEntity.ok("Call logs received successfully.");
    }

    // GET /api/calls/logs?fromDate=YYYY-MM-DD&toDate=YYYY-MM-DD&number=+1XXXXXXXXXX
    @GetMapping("/logs")
    public ResponseEntity<String> getCallLogs(
            @RequestParam String fromDate,
            @RequestParam String toDate,
            @RequestParam(required = false) String number) {

        if (fromDate.isEmpty() || toDate.isEmpty()) {
            return ResponseEntity.badRequest().body("fromDate and toDate are required.");
        }

        try {
            return callHippoClient.getCallLogs(fromDate, toDate, number);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching call logs: " + e.getMessage());
        }
    }

    // GET /api/calls/summary?fromDate=YYYY-MM-DD&toDate=YYYY-MM-DD&number=+1XXXXXXXXXX
    @GetMapping("/summary")
    public ResponseEntity<CallLogSummary> getCallLogSummary(
            @RequestParam String fromDate,
            @RequestParam String toDate,
            @RequestParam(required = false) String number) {

        try {
            List<CallLogResponse> callLogs = callHippoClient.getCallLogsAsObjects(fromDate, toDate, number);
            CallLogSummary summary = callLogService.summarizeCallLogs(callLogs);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // ✅ NEW ENDPOINT: GET /api/calls/filterbyuser?userId=+91XXXXXXXXXX&fromDate=YYYY-MM-DD&toDate=YYYY-MM-DD
    @GetMapping("/filterbyuser")
    public ResponseEntity<List<CallLogResponse>> getCallsByUserIdAndDateRange(
            @RequestParam String userId,
            @RequestParam String fromDate,
            @RequestParam String toDate) {

        if (userId == null || userId.isEmpty() || fromDate == null || toDate == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            List<CallLogResponse> allLogs = callHippoClient.getCallLogsAsObjects(fromDate, toDate, null);

            // Filter logs where user is either the caller or receiver
            List<CallLogResponse> filteredLogs = allLogs.stream()
                    .filter(log -> userId.equals(log.getMsisdn()) || userId.equals(log.getCalledNumber()))
                    .toList();

            return ResponseEntity.ok(filteredLogs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
