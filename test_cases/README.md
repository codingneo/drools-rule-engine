Before running each Test Case, run "curl -H "Content-Type: application/json" -X POST http://54.169.193.163:8080/clear" to reset transactions and users


1. test_case1 to test Usage rule: If for a given user ID > 5 good_txns AND total spend > 1000 then eligible (refer to https://github.com/ahhuisg/PSRuleEngine/blob/master/src/main/resources/rules/Usage.drl)

	1.1 curl -H "Content-Type: application/json" -X POST --data @test_case1/transaction1.json http://54.169.193.163:8080/transact 

		User U1 doesn't have 5 good transaction and his total spend is less than 1000, so the the status is REVIEW

	1.2 curl -H "Content-Type: application/json" -X POST --data @test_case1/user1_update.json http://54.169.193.163:8080/user

	     Updae U1's good transaction number to 5 and total spend to 1001

	1.3 curl -H "Content-Type: application/json" -X POST --data @test_case1/transaction1.json http://54.169.193.163:8080/transact

		The transaction become ELIGIBLE



2. test_case2 to test Velocity rule: If for a given user ID > 3 attempts within 3 minutes, then not eligible (refer to https://github.com/ahhuisg/PSRuleEngine/blob/master/src/main/resources/rules/Velocity.drl)


	2.1 curl -H "Content-Type: application/json" -X POST --data @test_case2/transaction1.json http://54.169.193.163:8080/transact 

		User U1 fire a transaction at 27/07/2015 10:00:00. Transaction status is REVIEW

	2.2 curl -H "Content-Type: application/json" -X POST --data @test_case2/transaction2.json http://54.169.193.163:8080/user

	    User U1 fire a transaction at 27/07/2015 10:00:30. Transaction status is REVIEW

	2.3 curl -H "Content-Type: application/json" -X POST --data @test_case2/transaction3.json http://54.169.193.163:8080/transact

		User U1 fire a transaction at 27/07/2015 10:01:00. Transaction status is REVIEW

	2.4 curl -H "Content-Type: application/json" -X POST --data @test_case2/transaction4.json http://54.169.193.163:8080/transact

		User U1 fire a transaction at 27/07/2015 10:01:30. Transaction status is REJECT

	2.5 curl -H "Content-Type: application/json" -X POST --data @test_case2/transaction5.json http://54.169.193.163:8080/transact

		User U1 fire a transaction at 27/07/2015 10:10:00. Transaction status is REVIEW