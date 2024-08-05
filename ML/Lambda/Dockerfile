# Use a slim version of the base Python image to reduce the final image size
FROM python:3.11-slim

# Define custom function directory
ARG FUNCTION_DIR="/function"

RUN pip install --upgrade pip
RUN mkdir -p ${FUNCTION_DIR}
COPY requirements.txt ${FUNCTION_DIR}
COPY lambda_docker/lambda_function.py ${FUNCTION_DIR}
COPY lambda_docker/model_function.py ${FUNCTION_DIR}
COPY lambda_docker/rec_camping_function.py ${FUNCTION_DIR}

# Set working directory to function root directory
WORKDIR ${FUNCTION_DIR}

# Install the runtime interface client
RUN pip install awslambdaric

# Install requirements.txt
RUN pip install -r requirements.txt

# Set runtime interface client as default command for the container runtime
ENTRYPOINT [ "/usr/local/bin/python", "-m", "awslambdaric" ]
# entrypoint 실행시 argument로써 전달
CMD [ "lambda_function.handler" ]