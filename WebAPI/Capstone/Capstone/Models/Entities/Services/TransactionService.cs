using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.Models.Entities.Services
{
    public partial interface ITransactionService
    {
        IQueryable<Transaction> GetTransactionByUserId(string userId);
        //int CreateTransaction(Transaction model);
    }

    public partial class TransactionService
    {
        public IQueryable<Transaction> GetTransactionByUserId(string userId)
        {
            var transactions = this.Get(q => q.AspNetUserId == userId);
            return transactions;
        }

        //public int CreateTransaction(Transaction model)
        //{

        //}
    }
}