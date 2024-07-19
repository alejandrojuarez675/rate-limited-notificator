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

I develop an interface 'NotificationService' that allow you to use the two productive implementations:
- NotificatorServiceImpl
- RateLimitedNotificationServiceImpl

The first allow you to use the functionality without limitations and the second have a configuration that control the quantity of notification per user in certain time.

I try to don't use any framework to do easily the reading and knowledge of code, so for example, I have to develop mocks for tests.

### How to run
There is not a main java function to test it, but have test to apply the main logic, and you can read it to know how to use the class.

I don't do any test on domain, gateway and repository package due to they don't have any logic. So you only have tests on service package