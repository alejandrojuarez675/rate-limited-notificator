# Rate Limited Notificator

## Problem
We have a Notification system that sends out email notifications of various types (status update, daily news, project invitations, etc). We need to protect recipients from getting too many emails, either due to system errors or due to abuse, so let’s limit the number of emails sent to them by implementing a rate-limited version of NotificationService.

The system must reject requests that are over the limit.

Some sample notification types and rate limit rules, e.g.:
 - Status: not more than 2 per minute for each recipient 
 - News: not more than 1 per day for each recipient 
 - Marketing: not more than 3 per hour for each recipient 
 - Etc. these are just samples, the system might have several rate limit rules!

## Solution
### Description

I create the 'NotificationService' interface, which lets you utilize the two effective implementations:
- NotificationServiceImpl
- RateLimitedNotificationServiceImpl

The first gives you unrestricted access to the capability, while the second has a setting that lets you manage how many notifications each user receives within a given window of time.

I make an effort not to utilize any frameworks that make code reading and knowing easier; as an example, I have to create test mocks.

### How to run
You can read the test to learn how to use the class even though there isn't a main Java function to test it. It applies the primary logic.

Because the domain, gateway, and repository packages lack logic, I don't test them. You therefore just have service package tests.

Since this project uses Maven, you can run it in the terminal:

```
$ mvn test
```

Alternately, you could just run the test in an IDE like Intellij.