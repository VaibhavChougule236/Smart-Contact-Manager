console.log("It is Javascript file")

const toggleSidebar = () => {
	if ($(".sidebar").is(":visible")) {
		$(".sidebar").css("display", "none")
		$(".content").css("margin-left", "2%")
	} else {
		$(".sidebar").css("display", "block")
		$(".content").css("margin-left", "21%")
	}
	/*	if($(".sidebar").css("width","20%")){
				$(".sidebar").css("width","7%")
				$(".content").css("margin-left","8%")
			}else{
				$(".sidebar").css("display","block")
				$(".sidebar").css("width","20%")
				$(".content").css("margin-left","20%")
			}*/
};

const search = () => {
	console.log("searching ...");
	let query = $("#search-input").val();
	if (query == "") {
		$(".search-result").hide();

	} else {
		console.log(query);

		let url = `http://localhost:8285/search/${query}`;
		fetch(url).then(response => {
			return response.json();
		})
			.then((data) => {
				//(parameter) data:any
				console.log(data);
				let text = `<div class='list-group'>`;
				data.forEach((contact) => {
					text += `<a href='/user/${contact.cid}/contact_Detail' class='list-group-item list-group-item-action'> ${contact.name} </a>`

				});
				text += `</div>`;

				$(".search-result").html(text);
				$(".search-result").show();
			});

	}
};


/*const paymentStart = () => {
	console.log("Payment Started...");

	// Get the payment amount from the input field
	let amount = $("#payment_field").val();
	console.log("Amount entered: ", amount);

	// Validate the amount
	if (amount == '' || amount == null) {
		alert("Please enter a valid amount!");
		return;
	}

	// Send the POST request to create an order on the server
	fetch('/user/create_order', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			amount: amount,
			info: 'order_request'
		})
	})
		.then(response => response.json()) // Assuming the response is JSON
		.then(data => {
			console.log("Order created: ", data);

			if (!data || !data.id) {
				alert("Error creating order. Please try again.");
				return;
			}

			// Razorpay options
			const options = {
				key: "rzp_test_3uqyjTXiuGQxxP", // Replace with your Razorpay Key ID
				amount: data.amount, // Amount in currency subunits
				currency: "INR",
				name: "Acme Corp", // Replace with your company name
				description: "Test Transaction",
				//image: "https://example.com/your_logo", // Optional: Company logo URL
				order_id: data.id, // Pass the `id` obtained in the response
				handler: function(response) {
					// Handle successful payment
					console.log("Payment successful: ", response);
					alert("Payment Successful!\nPayment ID: " + response.razorpay_payment_id);
				},
				prefill: {
					name: "Vaibhav", // Optional: Prefilled user details
					email: "vaibhavchougule236@gmail.com",
					contact: "8010625721"
				},
				notes: {
					address: "Razorpay Corporate Office"
				},
				theme: {
					color: "#3399cc"
				}
			};

			// Initialize Razorpay
			const rzp1 = new Razorpay(options);

			// Handle payment failure
			rzp1.on('payment.failed', function(response) {
				console.log("Payment failed: ", response);
				alert("Payment Failed:\n" + response.error.description);
			});

			// Open the Razorpay payment form
			rzp1.open();
		})
		.catch(error => {
			console.error("Error creating order: ", error);
			alert("Something went wrong. Please try again.");
		});
};

*/