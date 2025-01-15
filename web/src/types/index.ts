export type LoginOption = {
  label: string;
  loginUri: string;
  isSameAuthority: boolean;
};

export type MyData = {
  attributes: MyDataAttributes;
};

export type MyDataAttributes = {
  email: string;
  name: string;
};
