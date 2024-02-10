async function generateMonthlyReports() {
    // Display loading message
    document.getElementById('loadingMessage').innerText = 'Loading... Please wait';

    const xArray = [];
    const yArray = [];

    try {
        const response = await fetch(`/admin/getReports`);
        const data = await response.json();

        for (const key in data) {
            if (data.hasOwnProperty(key)) {
                xArray.push(key);
                yArray.push(data[key]);
            }
        }

        // Hide loading message and display the chart
        await new Promise(resolve => setTimeout(resolve, 200));
        document.getElementById('loadingMessage').style.display = 'none';
        document.getElementById('loadingSpinner').style.display = 'none';
        document.getElementById('myPlot').style.display = 'block';

        // You can print the arrays to the console if needed
        for (let i = 0; i < xArray.length; i++) {
            console.log(xArray[i] + " : " + yArray[i]);
        }

        const dataPlot = [{
            x: xArray,
            y: yArray,
            type: "bar",
            orientation: "v",
            marker: {color: "rgba(0,0,255,0.6)"}
        }];

        const layout = {title: "Daily Sale Reports"};
        Plotly.newPlot("myPlot", dataPlot, layout);

    } catch (e) {
        // Handle errors and update the loading message
        document.getElementById('loadingMessage').innerText = 'Error loading data.';
        console.error("Error:", e.toString());
    }
}