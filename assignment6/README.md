# IN3110-maryahai - assignment6

**requirment**

For this assignment you have to install:

 pip install altair_viewer



**TASK 6.1:**

This task Creates pandas dataframe from .csv file. Data will be filtered based on data column name "new_cases_smoothed_per_million", list of countries to be plotted and time frame chosen.
You can change the parameter countries, start and end date on webvisualization_plots.py-file in the main()-method.
Run the task by writing the following command in your terminal:
python3  webvisualization_plots.py
or 
python  webvisualization_plots.py

 **TASK 6.2:**

Here i build a FastAPI app which uses script webvisualization_plots.py from 6.1 to generate a plot of ”Daily new confirmed COVID-19 cases per million people” by date and display it on the web page. 
You can change the input-parameter countries, start and end date on webvisualization.py in plot_reported_cases_per_million_html()-method.
Here you also can run the task by writing the following command in your terminal:
python3 webvisualization.py

 **Task 6.3:**

Here we modify the solution from 6.2 so that the visitor of the web page can change the countries shown in the plot using a checkbox menu. The menu will allow the user to add the different countries to the interactive plot. I coudn't get the time range to change! but i made a slider for it.
Run the task by writing the following command in your terminal:
python3 webvisualization.py


 **TASK 6.5:**

Here i add navigationbar with a help-page. Furthermore, i added a link to the FastAPI docs, that are created automatically, and a link to help-page on the plot page. And add all to a navigation bar using code in Assignment.
The help page display help for functions implemented to generate
the plot shown. The help page is generated automatically from docstrings using Sphinx. The code is on docs-directory.
Run the task by writing the following command in your terminal:
python3 webvisualization.py

 **TASK 6.6:**

Here we should add Cumulative Cases in the App. I wrote a new method in 
webvisualization_plots.py with named total_cases_per_million(), to extract data from .csv and make a mark_circle-chart. And I wrote another method in webvisualization.py, total_cases_per_million_json()-method. But i couldn't put it on webApp with a dropdown-menu and it doesn't work!
python3 webvisualization.py
