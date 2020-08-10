import React, { Component } from "react";
import PlaidAuthenticator from 'react-native-plaid-link';
import { StyleSheet, Text, View, TouchableOpacity } from 'react-native';


export default class Plaid extends React.Component {
  state = {
    data: {},
    status: 'NOT_CONNECTED'
  };

  render() {
    console.log(this.state.status)
    const { navigation } = this.props;

    switch(this.state.status) {
      case 'CONNECTED':
        console.log('connected');
        console.log(this.state.data.metadata.public_token);
        return this.renderDetails();
        // navigation.navigate("Dashboard");
      case 'NOT_CONNECTED':
        return this.renderLogin();
      default:
        return this.renderLogin();
    }
  }

  renderDetails() {
    return (
      <View style={styles.container}>
        <Text style={styles.paragraph}>Institution</Text>
        <Text style={styles.value}>
          {this.state.data.metadata.institution.name}
        </Text>
        <Text style={styles.paragraph}>Institution ID</Text>
        <Text style={styles.value}>
          {this.state.data.metadata.institution.institution_id}
        </Text>
        <Text style={styles.paragraph}>Token</Text>
        <Text style={styles.value}>
          {this.state.data.metadata.public_token}
        </Text>
      </View>
    );
  }

  onMessage = data => {
    // console.log(data)
    /*
      Response example for success
      {
        "action": "plaid_link-undefined::connected",
        "metadata": {
          "account": {
            "id": null,
            "name": null
          },
          "account_id": null,
          "public_token": "public-sandbox-e697e666-9ac2-4538-b152-7e56a4e59365",
          "institution": {
            "name": "Chase",
            "institution_id": "ins_3"
          }
        }
      }
    */
    this.setState({ data, status: data.action.substr(data.action.lastIndexOf(':') + 1).toUpperCase() });
  };
  
  renderLogin() {
    return (
      <PlaidAuthenticator
        onMessage={this.onMessage}
        publicKey="eecc6d6382543dbee6478afbc5879b"
        env="sandbox"
        product="auth,transactions"
        onLoad={this.onLoad}
        onLoadStart={this.onLoadStart}
        onLoadEnd={this.onLoadEnd}
      />
    );
  };
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    paddingTop: 24,
    backgroundColor: '#ecf0f1'
  },
  paragraph: {
    fontSize: 18,
    marginBottom: 5,
    fontWeight: 'bold',
    textAlign: 'center',
    color: '#34495e'
  },
  value: {
    marginBottom: 20,
    textAlign: 'center'
  }
});