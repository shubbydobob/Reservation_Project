document.addEventListener("DOMContentLoaded", () => {
  const seatMap = document.getElementById("seat-map");
  const selectedSeatsList = document.getElementById("selected-seats");
  const reserveButton = document.getElementById("reserve-button");
  const concertDate = document.getElementById("concert-date");
  const concertTime = document.getElementById("concert-time");
  const concertNameInput = document.getElementById("concert-name");
  const concertNameHiddenInput = document.getElementById("concert-name-input");

  const urlParams = new URLSearchParams(window.location.search);
  const concertDateFromURL = urlParams.get('date');
  const concertTimeFromURL = urlParams.get('time');
  const concertNameFromURL = urlParams.get('concertName');

  // Check if the concert name exists in the URL params
  if (concertNameFromURL) {
    if (concertNameInput) {
      concertNameInput.value = concertNameFromURL;
    }
    if (concertNameHiddenInput) {
      concertNameHiddenInput.value = concertNameFromURL;  // Set hidden input
    }
  } else {
    if (concertNameInput) {
      concertNameInput.value = "Concert XYZ"; // Default concert name
    }
    if (concertNameHiddenInput) {
      concertNameHiddenInput.value = "Concert XYZ";  // Default hidden value
    }
  }

  // Set the date and time from the URL or use default values
  if (concertDateFromURL) {
    concertDate.value = concertDateFromURL;
  } else {
    concertDate.value = "2024-01-15"; // 기본값
  }

  if (concertTimeFromURL) {
    concertTime.value = concertTimeFromURL;
  } else {
    concertTime.value = "18:00"; // 기본값
  }

  // Create seats dynamically
  const seats = Array.from({ length: 100 }, (_, i) => ({
    id: i + 1,
    status: "available"
  }));

  // Loop through each seat and create corresponding seat elements
  seats.forEach(seat => {
    const seatElement = document.createElement("div");
    seatElement.classList.add("seat", seat.status);
    seatElement.textContent = seat.id;
    seatElement.dataset.id = seat.id;

    // Add click event listener to toggle seat selection
    if (seat.status === "available") {
      seatElement.addEventListener("click", () => toggleSeatSelection(seat, seatElement));
    }

    seatMap.appendChild(seatElement);
  });

  const selectedSeats = [];

  // Function to toggle seat selection
  function toggleSeatSelection(seat, seatElement) {
    if (selectedSeats.includes(seat.id)) {
      selectedSeats.splice(selectedSeats.indexOf(seat.id), 1);
      seatElement.classList.remove("selected");
    } else {
      selectedSeats.push(seat.id);
      seatElement.classList.add("selected");
    }
    updateSummary();
  }

  // Function to update the summary
  function updateSummary() {
    selectedSeatsList.innerHTML = selectedSeats.map(seatId => `<li>좌석 ${seatId}</li>`).join("");
    document.getElementById("selected-seats-input").value = selectedSeats.join(",");
    reserveButton.disabled = selectedSeats.length === 0 || !concertDate.value || !concertTime.value;

    document.getElementById("summary-date").textContent = concertDate.value;
    document.getElementById("summary-time").textContent = concertTime.value;
    document.getElementById("summary-concert-name").textContent = concertNameInput.value;
  }

  // Event listeners for date and time change
  concertDate.addEventListener("change", updateSummary);
  concertTime.addEventListener("change", updateSummary);

  // Initial summary update
  updateSummary();
});